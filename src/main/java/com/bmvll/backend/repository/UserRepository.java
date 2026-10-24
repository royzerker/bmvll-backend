package com.bmvll.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.bmvll.backend.model.DocumentType;
import com.bmvll.backend.model.User;
import com.bmvll.backend.model.UserRole;

public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByRole(UserRole role);

    boolean existsByDocumentTypeAndDocumentNumber(DocumentType documentType, String documentNumber);

    List<User> findByRoleOrderByLastNameAsc(UserRole role);
}
