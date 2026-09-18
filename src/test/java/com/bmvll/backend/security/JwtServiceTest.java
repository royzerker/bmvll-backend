package com.bmvll.backend.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

import com.bmvll.backend.model.User;
import com.bmvll.backend.model.UserRole;

import io.jsonwebtoken.JwtException;

class JwtServiceTest {

    private final JwtService jwtService = new JwtService(
            "test-secret-key-at-least-256-bits-long-for-hmac-sha-algorithm",
            8 * 60 * 60 * 1000L);

    private User librarian() {
        return User.builder()
                .id("64f000000000000000000001")
                .email("librarian@bmvll.local")
                .role(UserRole.LIBRARIAN)
                .build();
    }

    @Test
    void generatesTokenWithReadableClaims() {
        String token = jwtService.generateToken(librarian());

        var claims = jwtService.parseClaims(token);

        assertThat(claims.getSubject()).isEqualTo("64f000000000000000000001");
        assertThat(claims.get("email", String.class)).isEqualTo("librarian@bmvll.local");
        assertThat(claims.get("role", String.class)).isEqualTo("LIBRARIAN");
    }

    @Test
    void rejectsTokenSignedWithADifferentSecret() {
        JwtService otherService = new JwtService(
                "a-completely-different-secret-key-also-256-bits-long-enough",
                8 * 60 * 60 * 1000L);
        String token = otherService.generateToken(librarian());

        assertThatThrownBy(() -> jwtService.parseClaims(token)).isInstanceOf(JwtException.class);
    }

    @Test
    void rejectsExpiredToken() {
        JwtService expiredService = new JwtService(
                "test-secret-key-at-least-256-bits-long-for-hmac-sha-algorithm",
                -1000L);
        String token = expiredService.generateToken(librarian());

        assertThatThrownBy(() -> jwtService.parseClaims(token)).isInstanceOf(JwtException.class);
    }
}
