package com.bmvll.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.bmvll.backend.dto.LoanRequest;
import com.bmvll.backend.dto.LoanResponse;
import com.bmvll.backend.model.Copy;
import com.bmvll.backend.model.CopyStatus;
import com.bmvll.backend.model.Loan;
import com.bmvll.backend.model.LoanStatus;
import com.bmvll.backend.model.User;
import com.bmvll.backend.model.UserStatus;
import com.bmvll.backend.repository.CopyRepository;
import com.bmvll.backend.repository.LoanRepository;
import com.bmvll.backend.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class LoanServiceTest {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private CopyRepository copyRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private LoanService loanService;

    private final User member = User.builder().id("u1").status(UserStatus.ACTIVE).build();

    @Test
    void create_prestaEjemplarDisponible() {
        Copy copy = Copy.builder().id("c1").bookId("b1").status(CopyStatus.AVAILABLE).build();
        when(userRepository.findById("u1")).thenReturn(Optional.of(member));
        when(copyRepository.findByBookIdAndStatus("b1", CopyStatus.AVAILABLE)).thenReturn(List.of(copy));
        when(userRepository.findByEmail("lib@bmvll.local"))
                .thenReturn(Optional.of(User.builder().id("lib1").build()));
        when(loanRepository.save(any(Loan.class))).thenAnswer(inv -> inv.getArgument(0));

        Loan loan = loanService.create(new LoanRequest("u1", "b1", 7, null), "lib@bmvll.local");

        assertEquals(CopyStatus.LOANED, copy.getStatus());
        assertEquals("c1", loan.getCopyId());
        assertEquals("lib1", loan.getLibrarianId());
        assertEquals(LoanStatus.ACTIVE, loan.getStatus());
        assertEquals(Duration.ofDays(7), Duration.between(loan.getLoanDate(), loan.getDueDate()));
    }

    @Test
    void create_sinDisponibilidad_lanzaConflict() {
        when(userRepository.findById("u1")).thenReturn(Optional.of(member));
        when(copyRepository.findByBookIdAndStatus("b1", CopyStatus.AVAILABLE)).thenReturn(List.of());

        var ex = assertThrows(ResponseStatusException.class,
                () -> loanService.create(new LoanRequest("u1", "b1", null, null), "lib@bmvll.local"));

        assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
        verify(loanRepository, never()).save(any());
    }

    @Test
    void create_socioSuspendido_lanzaConflict() {
        User suspended = User.builder().id("u1").status(UserStatus.SUSPENDED).build();
        when(userRepository.findById("u1")).thenReturn(Optional.of(suspended));

        var ex = assertThrows(ResponseStatusException.class,
                () -> loanService.create(new LoanRequest("u1", "b1", null, null), "lib@bmvll.local"));

        assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
    }

    @Test
    void returnLoan_liberaEjemplar() {
        Loan loan = Loan.builder().id("l1").copyId("c1").status(LoanStatus.ACTIVE).build();
        Copy copy = Copy.builder().id("c1").status(CopyStatus.LOANED).build();
        when(loanRepository.findById("l1")).thenReturn(Optional.of(loan));
        when(copyRepository.findById("c1")).thenReturn(Optional.of(copy));
        when(loanRepository.save(any(Loan.class))).thenAnswer(inv -> inv.getArgument(0));

        Loan returned = loanService.returnLoan("l1");

        assertEquals(LoanStatus.RETURNED, returned.getStatus());
        assertNotNull(returned.getReturnDate());
        assertEquals(CopyStatus.AVAILABLE, copy.getStatus());
    }

    @Test
    void returnLoan_yaDevuelto_lanzaConflict() {
        Loan loan = Loan.builder().id("l1").status(LoanStatus.RETURNED).build();
        when(loanRepository.findById("l1")).thenReturn(Optional.of(loan));

        var ex = assertThrows(ResponseStatusException.class, () -> loanService.returnLoan("l1"));

        assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
    }

    @Test
    void response_prestamoVencido_seMuestraOverdue() {
        Instant now = Instant.now();
        Loan loan = Loan.builder().id("l1").status(LoanStatus.ACTIVE).dueDate(now.minus(Duration.ofDays(1))).build();

        assertEquals(LoanStatus.OVERDUE, LoanResponse.from(loan, now).status());
    }
}
