package com.bmvll.backend.dto;

import java.util.List;

import com.bmvll.backend.model.Book;

public record BookResponse(
        String id,
        String title,
        List<String> authors,
        String isbn,
        String categoryId,
        String publisher,
        Integer publicationYear,
        String language,
        String synopsis,
        long totalCopies,
        long availableCopies) {

    public static BookResponse from(Book book, long totalCopies, long availableCopies) {
        return new BookResponse(
                book.getId(),
                book.getTitle(),
                book.getAuthors(),
                book.getIsbn(),
                book.getCategoryId(),
                book.getPublisher(),
                book.getPublicationYear(),
                book.getLanguage(),
                book.getSynopsis(),
                totalCopies,
                availableCopies);
    }
}
