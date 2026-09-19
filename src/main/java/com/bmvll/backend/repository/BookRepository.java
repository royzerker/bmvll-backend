package com.bmvll.backend.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.bmvll.backend.model.Book;

public interface BookRepository extends MongoRepository<Book, String> {
    List<Book> findByActiveTrue();
}
