package com.bmvll.backend.service;

import java.time.Instant;
import java.util.List;
import java.util.Locale;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.bmvll.backend.dto.MemberRequest;
import com.bmvll.backend.model.User;
import com.bmvll.backend.model.UserRole;
import com.bmvll.backend.model.UserStatus;
import com.bmvll.backend.repository.UserRepository;

/** Socios = usuarios con rol MEMBER (sin login en esta fase, ver TODO.md). */
@Service
public class MemberService {

    private final UserRepository userRepository;

    public MemberService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /** Filtro en memoria: el padrón de socios de la biblioteca es pequeño. */
    public List<User> search(String query) {
        List<User> members = userRepository.findByRoleOrderByLastNameAsc(UserRole.MEMBER);
        if (query == null || query.isBlank()) {
            return members;
        }
        String normalized = query.trim().toLowerCase(Locale.ROOT);
        return members.stream()
                .filter(member -> matches(member, normalized))
                .toList();
    }

    public User create(MemberRequest request) {
        String documentNumber = request.documentNumber().trim();
        String email = request.email().trim().toLowerCase(Locale.ROOT);

        if (userRepository.existsByDocumentTypeAndDocumentNumber(request.documentType(), documentNumber)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un socio con ese documento");
        }
        if (userRepository.existsByEmail(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El email ya está registrado");
        }

        Instant now = Instant.now();
        User member = User.builder()
                .documentType(request.documentType())
                .documentNumber(documentNumber)
                .firstName(request.firstName().trim())
                .lastName(request.lastName().trim())
                .email(email)
                .phone(request.phone())
                .role(UserRole.MEMBER)
                .status(UserStatus.ACTIVE)
                .createdAt(now)
                .updatedAt(now)
                .build();

        return userRepository.save(member);
    }

    private static boolean matches(User member, String query) {
        String fullName = (member.getFirstName() + " " + member.getLastName()).toLowerCase(Locale.ROOT);
        return fullName.contains(query)
                || (member.getDocumentNumber() != null && member.getDocumentNumber().contains(query))
                || (member.getEmail() != null && member.getEmail().toLowerCase(Locale.ROOT).contains(query));
    }
}
