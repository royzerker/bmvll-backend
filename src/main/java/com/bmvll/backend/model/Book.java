package com.bmvll.backend.model;

import java.time.Instant;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("books")
public class Book {

    @Id
    private String id;

    private String title;
    private List<String> authors;

    @Indexed(unique = true)
    private String isbn;

    @Indexed
    private String categoryId;

    private String publisher;
    private Integer publicationYear;
    private String language;
    private String synopsis;

    private boolean active;

    private Instant createdAt;
    private Instant updatedAt;
}
