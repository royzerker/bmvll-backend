package com.bmvll.backend.service;

import java.time.Instant;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.bmvll.backend.dto.CopyRequest;
import com.bmvll.backend.model.Copy;
import com.bmvll.backend.model.CopyStatus;
import com.bmvll.backend.model.Location;
import com.bmvll.backend.repository.BookRepository;
import com.bmvll.backend.repository.CopyRepository;

@Service
public class CopyService {

    /** LOANED y RESERVED los asignan préstamos/reservas, no se cambian a mano. */
    private static final Set<CopyStatus> MANUAL_STATUSES =
            EnumSet.of(CopyStatus.AVAILABLE, CopyStatus.IN_REPAIR, CopyStatus.LOST, CopyStatus.WITHDRAWN);

    private final CopyRepository copyRepository;
    private final BookRepository bookRepository;

    public CopyService(CopyRepository copyRepository, BookRepository bookRepository) {
        this.copyRepository = copyRepository;
        this.bookRepository = bookRepository;
    }

    public List<Copy> findByBookId(String bookId) {
        return copyRepository.findByBookId(bookId);
    }

    public Copy create(CopyRequest request) {
        bookRepository.findById(request.bookId())
                .filter(book -> book.isActive())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Libro no encontrado"));

        String inventoryCode = request.inventoryCode().trim();
        if (copyRepository.existsByInventoryCode(inventoryCode)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un ejemplar con ese código de inventario");
        }

        Copy copy = Copy.builder()
                .bookId(request.bookId())
                .inventoryCode(inventoryCode)
                .status(CopyStatus.AVAILABLE)
                .condition(request.condition())
                .location(new Location(request.branch(), request.shelf()))
                .acquisitionDate(Instant.now())
                .build();

        return copyRepository.save(copy);
    }

    public Copy updateStatus(String id, CopyStatus status) {
        if (!MANUAL_STATUSES.contains(status)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El estado " + status + " lo asigna el préstamo o la reserva");
        }

        Copy copy = copyRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ejemplar no encontrado"));
        if (copy.getStatus() == CopyStatus.LOANED) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El ejemplar está prestado; registre primero la devolución");
        }

        copy.setStatus(status);
        return copyRepository.save(copy);
    }
}
