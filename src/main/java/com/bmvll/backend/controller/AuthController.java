package com.bmvll.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bmvll.backend.dto.LoginRequest;
import com.bmvll.backend.dto.LoginResponse;
import com.bmvll.backend.dto.RegisterRequest;
import com.bmvll.backend.dto.UserResponse;
import com.bmvll.backend.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    /** Solo ADMIN (ver SecurityConfig) — crea cuentas ADMIN/LIBRARIAN adicionales. */
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        var created = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponse.from(created));
    }
}
