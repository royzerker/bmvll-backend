package com.bmvll.backend.dto;

import com.bmvll.backend.model.DocumentType;
import com.bmvll.backend.model.User;
import com.bmvll.backend.model.UserStatus;

public record MemberResponse(
        String id,
        DocumentType documentType,
        String documentNumber,
        String firstName,
        String lastName,
        String email,
        String phone,
        UserStatus status) {

    public static MemberResponse from(User user) {
        return new MemberResponse(
                user.getId(),
                user.getDocumentType(),
                user.getDocumentNumber(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhone(),
                user.getStatus());
    }
}
