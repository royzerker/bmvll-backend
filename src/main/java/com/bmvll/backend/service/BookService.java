package com.bmvll.backend.service;

import java.time.Instant;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.bmvll.backend.dto.BookRequest;
import com.bmvll.backend.dto.BookResponse;
import com.bmvll.backend.model.Book;
import com.bmvll.backend.model.CopyStatus;
import com.bmvll.backend.repository.BookRepository;
import com.bmvll.backend.repository.CategoryRepository;
import com.bmvll.backend.repository.CopyRepository;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final CopyRepository copyRepository;
    private final CategoryRepository categoryRepository;

    public BookService(
            BookRepository bookRepository,
            CopyRepository copyRepository,
            CategoryRepository categoryRepository) {
        this.bookRepository = bookRepository;
        this.copyRepository = copyRepository;
        this.categoryRepository = categoryRepository;
    }

    public BookResponse create(BookRequest request) {
        if (!categoryRepository.existsById(request.categoryId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La categoría indicada no existe");
        }
        if (bookRepository.existsByIsbn(request.isbn())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un libro con ese ISBN");
        }

        Instant now = Instant.now();
        Book book = new Book();
        book.setTitle(request.title());
        book.setAuthors(request.authors());
        book.setIsbn(request.isbn());
        book.setCategoryId(request.categoryId());
        book.setPublisher(request.publisher());
        book.setPublicationYear(request.publicationYear());
        book.setLanguage(request.language());
        book.setSynopsis(request.synopsis());
        book.setActive(true);
        book.setCreatedAt(now);
        book.setUpdatedAt(now);

        Book saved = bookRepository.save(book);
        return toResponse(saved);
    }

    public void delete(String id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Libro no encontrado"));
        book.setActive(false);
        book.setUpdatedAt(Instant.now());
        bookRepository.save(book);
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
