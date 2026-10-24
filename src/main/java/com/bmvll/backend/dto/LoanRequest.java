package com.bmvll.backend.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** {@code days} es opcional: si no viene se usa el plazo por defecto (14 días). */
public record LoanRequest(
        @NotBlank String userId,
        @NotBlank String bookId,
        @Min(1) @Max(30) Integer days,
        @Size(max = 500) String notes) {
}
