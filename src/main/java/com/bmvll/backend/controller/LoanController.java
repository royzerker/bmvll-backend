package com.bmvll.backend.controller;

import java.time.Instant;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bmvll.backend.dto.LoanRequest;
import com.bmvll.backend.dto.LoanResponse;
import com.bmvll.backend.service.LoanService;

import jakarta.validation.Valid;

/** Préstamos y devoluciones: requiere ADMIN/LIBRARIAN (ver SecurityConfig). */
@RestController
@RequestMapping("/api/loans")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    /** Con {@code userId}: historial del socio. Sin él: todos los préstamos activos. */
    @GetMapping
    public List<LoanResponse> list(@RequestParam(required = false) String userId) {
        Instant now = Instant.now();
        var loans = userId != null ? loanService.findByUserId(userId) : loanService.findActive();
        return loans.stream().map(loan -> LoanResponse.from(loan, now)).toList();
    }

    @PostMapping
    public ResponseEntity<LoanResponse> create(@Valid @RequestBody LoanRequest request, Authentication authentication) {
        var created = loanService.create(request, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(LoanResponse.from(created, Instant.now()));
    }

    @PatchMapping("/{id}/return")
    public LoanResponse returnLoan(@PathVariable String id) {
        return LoanResponse.from(loanService.returnLoan(id), Instant.now());
    }
}
