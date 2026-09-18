package com.bmvll.backend.model;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
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
@Document("users")
@CompoundIndex(name = "document_unique", def = "{'documentType': 1, 'documentNumber': 1}", unique = true)
public class User {

    @Id
    private String id;

    private DocumentType documentType;
    private String documentNumber;

    private String firstName;
    private String lastName;

    @Indexed(unique = true)
    private String email;

    private String phone;
    private Address address;

    @Indexed
    private UserRole role;

    private UserStatus status;

    /** BCrypt hash. Solo se usa para roles ADMIN/LIBRARIAN por ahora. */
    private String passwordHash;

    private Instant createdAt;
    private Instant updatedAt;
}
