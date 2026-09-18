package com.bmvll.backend.dto;

import com.bmvll.backend.model.User;

public record UserResponse(
        String id,
        String email,
        String firstName,
        String lastName,
        String role,
        String status) {

    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole().name(),
                user.getStatus().name());
    }
}
