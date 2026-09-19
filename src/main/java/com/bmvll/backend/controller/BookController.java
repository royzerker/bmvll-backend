package com.bmvll.backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bmvll.backend.dto.BookResponse;
import com.bmvll.backend.service.BookService;

/** GET público (ver SecurityConfig) — catálogo visible sin login. */
@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public List<BookResponse> list() {
        return bookService.findAll();
    }

    @GetMapping("/{id}")
    public BookResponse get(@PathVariable String id) {
        return bookService.findById(id);
    }
}
