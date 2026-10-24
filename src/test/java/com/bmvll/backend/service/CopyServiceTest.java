package com.bmvll.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.bmvll.backend.dto.CopyRequest;
import com.bmvll.backend.model.Book;
import com.bmvll.backend.model.Copy;
import com.bmvll.backend.model.CopyCondition;
import com.bmvll.backend.model.CopyStatus;
import com.bmvll.backend.repository.BookRepository;
import com.bmvll.backend.repository.CopyRepository;

@ExtendWith(MockitoExtension.class)
class CopyServiceTest {

    @Mock
    private CopyRepository copyRepository;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private CopyService copyService;

    @Test
    void create_registraEjemplarDisponible() {
        Book book = Book.builder().id("b1").active(true).build();
        when(bookRepository.findById("b1")).thenReturn(Optional.of(book));
        when(copyRepository.existsByInventoryCode("INV-001")).thenReturn(false);
        when(copyRepository.save(any(Copy.class))).thenAnswer(inv -> inv.getArgument(0));

        Copy copy = copyService.create(new CopyRequest("b1", " INV-001 ", CopyCondition.NEW, "Sede Central", "A-3"));

        assertEquals("INV-001", copy.getInventoryCode());
        assertEquals(CopyStatus.AVAILABLE, copy.getStatus());
        assertEquals("A-3", copy.getLocation().getShelf());
    }

    @Test
    void create_codigoDuplicado_lanzaConflict() {
        Book book = Book.builder().id("b1").active(true).build();
        when(bookRepository.findById("b1")).thenReturn(Optional.of(book));
        when(copyRepository.existsByInventoryCode("INV-001")).thenReturn(true);

        var ex = assertThrows(ResponseStatusException.class,
                () -> copyService.create(new CopyRequest("b1", "INV-001", CopyCondition.NEW, null, null)));

        assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
        verify(copyRepository, never()).save(any());
    }

    @Test
    void create_libroInactivo_lanzaNotFound() {
        Book book = Book.builder().id("b1").active(false).build();
        when(bookRepository.findById("b1")).thenReturn(Optional.of(book));

        var ex = assertThrows(ResponseStatusException.class,
                () -> copyService.create(new CopyRequest("b1", "INV-001", CopyCondition.NEW, null, null)));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
    }

    @Test
    void updateStatus_loanedManual_lanzaBadRequest() {
        var ex = assertThrows(ResponseStatusException.class,
                () -> copyService.updateStatus("c1", CopyStatus.LOANED));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
    }

    @Test
    void updateStatus_ejemplarPrestado_lanzaConflict() {
        Copy copy = Copy.builder().id("c1").status(CopyStatus.LOANED).build();
        when(copyRepository.findById("c1")).thenReturn(Optional.of(copy));

        var ex = assertThrows(ResponseStatusException.class,
                () -> copyService.updateStatus("c1", CopyStatus.IN_REPAIR));

        assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
    }
}
