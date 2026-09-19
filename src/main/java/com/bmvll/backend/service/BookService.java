package com.bmvll.backend.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.bmvll.backend.dto.BookResponse;
import com.bmvll.backend.model.Book;
import com.bmvll.backend.model.CopyStatus;
import com.bmvll.backend.repository.BookRepository;
import com.bmvll.backend.repository.CopyRepository;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final CopyRepository copyRepository;

    public BookService(BookRepository bookRepository, CopyRepository copyRepository) {
        this.bookRepository = bookRepository;
        this.copyRepository = copyRepository;
    }

    public List<BookResponse> findAll() {
        return bookRepository.findByActiveTrue().stream()
                .map(this::toResponse)
                .toList();
    }

    public BookResponse findById(String id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Libro no encontrado"));
        return toResponse(book);
    }

    private BookResponse toResponse(Book book) {
        long total = copyRepository.countByBookId(book.getId());
        long available = copyRepository.countByBookIdAndStatus(book.getId(), CopyStatus.AVAILABLE);
        return BookResponse.from(book, total, available);
    }
}
