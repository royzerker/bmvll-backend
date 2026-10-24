package com.bmvll.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bmvll.backend.dto.CopyRequest;
import com.bmvll.backend.dto.CopyResponse;
import com.bmvll.backend.dto.CopyStatusRequest;
import com.bmvll.backend.service.CopyService;

import jakarta.validation.Valid;

/** Inventario físico: requiere ADMIN/LIBRARIAN (ver SecurityConfig). */
@RestController
@RequestMapping("/api/copies")
public class CopyController {

    private final CopyService copyService;

    public CopyController(CopyService copyService) {
        this.copyService = copyService;
    }

    @GetMapping
    public List<CopyResponse> listByBook(@RequestParam String bookId) {
        return copyService.findByBookId(bookId).stream().map(CopyResponse::from).toList();
    }

    @PostMapping
    public ResponseEntity<CopyResponse> create(@Valid @RequestBody CopyRequest request) {
        var created = copyService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(CopyResponse.from(created));
    }

    @PatchMapping("/{id}/status")
    public CopyResponse updateStatus(@PathVariable String id, @Valid @RequestBody CopyStatusRequest request) {
        return CopyResponse.from(copyService.updateStatus(id, request.status()));
    }
}
