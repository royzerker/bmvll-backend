package com.bmvll.backend.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record BookRequest(
        @NotBlank @Size(max = 300) String title,
        @NotEmpty List<@NotBlank String> authors,
        @NotBlank String isbn,
        @NotBlank String categoryId,
        String publisher,
        Integer publicationYear,
        String language,
        @Size(max = 2000) String synopsis) {
}
