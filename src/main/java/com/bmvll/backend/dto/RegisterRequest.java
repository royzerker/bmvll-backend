package com.bmvll.backend.dto;

import com.bmvll.backend.model.UserRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/** Solo ADMIN puede registrar nuevos ADMIN/LIBRARIAN (ver SecurityConfig). */
public record RegisterRequest(
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotBlank @Email String email,
        @NotBlank @Size(min = 8) String password,
        @NotNull UserRole role) {
}
