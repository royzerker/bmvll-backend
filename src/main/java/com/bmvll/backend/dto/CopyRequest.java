package com.bmvll.backend.dto;

import com.bmvll.backend.model.CopyCondition;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CopyRequest(
        @NotBlank String bookId,
        @NotBlank @Size(max = 50) String inventoryCode,
        @NotNull CopyCondition condition,
        String branch,
        String shelf) {
}
