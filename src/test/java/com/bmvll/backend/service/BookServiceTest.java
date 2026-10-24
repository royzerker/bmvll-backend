package com.bmvll.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import com.bmvll.backend.dto.BookResponse;
import com.bmvll.backend.model.Book;
import com.bmvll.backend.model.CopyStatus;
import com.bmvll.backend.repository.BookRepository;
import com.bmvll.backend.repository.CopyRepository;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private CopyRepository copyRepository;

    @InjectMocks
    private BookService bookService;

    @Test
    void searchByTitle_devuelveLibrosConDisponibilidad() {
        Book book = Book.builder().id("b1").title("Cien años de soledad").active(true).build();
        when(bookRepository.findByActiveTrueAndTitleContainingIgnoreCase("cien"))
                .thenReturn(List.of(book));
        when(copyRepository.countByBookId("b1")).thenReturn(3L);
        when(copyRepository.countByBookIdAndStatus("b1", CopyStatus.AVAILABLE)).thenReturn(2L);

        List<BookResponse> result = bookService.searchByTitle("  cien ");

        assertEquals(1, result.size());
        assertEquals("Cien años de soledad", result.get(0).title());
        assertEquals(3L, result.get(0).totalCopies());
        assertEquals(2L, result.get(0).availableCopies());
    }

    @Test
    void searchByTitle_tituloVacio_lanzaBadRequest() {
        assertThrows(ResponseStatusException.class, () -> bookService.searchByTitle("   "));
    }
}