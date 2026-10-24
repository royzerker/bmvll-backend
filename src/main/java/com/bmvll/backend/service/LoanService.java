package com.bmvll.backend.service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.bmvll.backend.dto.LoanRequest;
import com.bmvll.backend.model.Copy;
import com.bmvll.backend.model.CopyStatus;
import com.bmvll.backend.model.Loan;
import com.bmvll.backend.model.LoanStatus;
import com.bmvll.backend.model.User;
import com.bmvll.backend.model.UserStatus;
import com.bmvll.backend.repository.CopyRepository;
import com.bmvll.backend.repository.LoanRepository;
import com.bmvll.backend.repository.UserRepository;

@Service
public class LoanService {

    private static final int DEFAULT_LOAN_DAYS = 14;

    private final LoanRepository loanRepository;
    private final CopyRepository copyRepository;
    private final UserRepository userRepository;

    public LoanService(LoanRepository loanRepository, CopyRepository copyRepository, UserRepository userRepository) {
        this.loanRepository = loanRepository;
        this.copyRepository = copyRepository;
        this.userRepository = userRepository;
    }

    public List<Loan> findByUserId(String userId) {
        return loanRepository.findByUserIdOrderByLoanDateDesc(userId);
    }

    /** Presta el primer ejemplar AVAILABLE del libro; {@code librarianEmail} es el usuario autenticado. */
    public Loan create(LoanRequest request, String librarianEmail) {
        User member = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Socio no encontrado"));
        if (member.getStatus() != UserStatus.ACTIVE) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El socio no está activo");
        }

        Copy copy = copyRepository.findByBookIdAndStatus(request.bookId(), CopyStatus.AVAILABLE).stream()
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.CONFLICT, "No hay ejemplares disponibles de este libro"));

        String librarianId = userRepository.findByEmail(librarianEmail)
                .map(User::getId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Bibliotecario no encontrado"));

        copy.setStatus(CopyStatus.LOANED);
        copyRepository.save(copy);

        int days = request.days() != null ? request.days() : DEFAULT_LOAN_DAYS;
        Instant now = Instant.now();
        Loan loan = Loan.builder()
                .copyId(copy.getId())
                .bookId(request.bookId())
                .userId(member.getId())
                .librarianId(librarianId)
                .loanDate(now)
                .dueDate(now.plus(Duration.ofDays(days)))
                .status(LoanStatus.ACTIVE)
                .notes(request.notes())
                .build();

        return loanRepository.save(loan);
    }

    public Loan returnLoan(String id) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Préstamo no encontrado"));
        if (loan.getStatus() != LoanStatus.ACTIVE) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El préstamo no está activo");
        }

        copyRepository.findById(loan.getCopyId()).ifPresent(copy -> {
            copy.setStatus(CopyStatus.AVAILABLE);
            copyRepository.save(copy);
        });

        loan.setReturnDate(Instant.now());
        loan.setStatus(LoanStatus.RETURNED);
        return loanRepository.save(loan);
    }
}
