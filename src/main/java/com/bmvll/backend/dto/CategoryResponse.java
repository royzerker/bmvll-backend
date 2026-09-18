package com.bmvll.backend.dto;

import com.bmvll.backend.model.Category;

public record CategoryResponse(
        String id,
        String name,
        String description,
        boolean active) {

    public static CategoryResponse from(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.isActive());
    }
}