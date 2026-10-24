package com.bmvll.backend.dto;

import com.bmvll.backend.model.DocumentType;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MemberRequest(
        @NotNull DocumentType documentType,
        @NotBlank @Size(max = 20) String documentNumber,
        @NotBlank @Size(max = 100) String firstName,
        @NotBlank @Size(max = 100) String lastName,
        @NotBlank @Email String email,
        @Size(max = 20) String phone) {
}
