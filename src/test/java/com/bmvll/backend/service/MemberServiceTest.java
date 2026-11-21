package com.bmvll.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.bmvll.backend.dto.MemberRequest;
import com.bmvll.backend.model.DocumentType;
import com.bmvll.backend.model.User;
import com.bmvll.backend.model.UserRole;
import com.bmvll.backend.model.UserStatus;
import com.bmvll.backend.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private MemberService memberService;

    @Test
    void create_registraSocioActivo() {
        when(userRepository.existsByDocumentTypeAndDocumentNumber(DocumentType.DNI, "12345678")).thenReturn(false);
        when(userRepository.existsByEmail("ana@mail.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User member = memberService.create(
                new MemberRequest(DocumentType.DNI, " 12345678 ", "Ana", "Quispe", "Ana@Mail.com", null));

        assertEquals(UserRole.MEMBER, member.getRole());
        assertEquals(UserStatus.ACTIVE, member.getStatus());
        assertEquals("12345678", member.getDocumentNumber());
        assertEquals("ana@mail.com", member.getEmail());
    }

    @Test
    void create_documentoDuplicado_lanzaConflict() {
        when(userRepository.existsByDocumentTypeAndDocumentNumber(DocumentType.DNI, "12345678")).thenReturn(true);

        var ex = assertThrows(ResponseStatusException.class, () -> memberService.create(
                new MemberRequest(DocumentType.DNI, "12345678", "Ana", "Quispe", "ana@mail.com", null)));

        assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
        verify(userRepository, never()).save(any());
    }

    @Test
    void search_filtraPorNombreODocumento() {
        User ana = User.builder().firstName("Ana").lastName("Quispe").documentNumber("12345678").build();
        User luis = User.builder().firstName("Luis").lastName("Rojas").documentNumber("87654321").build();
        when(userRepository.findByRoleOrderByLastNameAsc(UserRole.MEMBER)).thenReturn(List.of(ana, luis));

        assertEquals(List.of(ana), memberService.search("quis"));
        assertEquals(List.of(luis), memberService.search("8765"));
        assertEquals(2, memberService.search("  ").size());
    }
}
