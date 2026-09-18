package com.bmvll.backend.dto;

public record LoginResponse(
        String token,
        String tokenType,
        long expiresInSeconds,
        String userId,
        String email,
        String firstName,
        String lastName,
        String role) {
}
