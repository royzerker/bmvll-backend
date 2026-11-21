package com.bmvll.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bmvll.backend.dto.MemberRequest;
import com.bmvll.backend.dto.MemberResponse;
import com.bmvll.backend.service.MemberService;

import jakarta.validation.Valid;

/** Padrón de socios: requiere ADMIN/LIBRARIAN (ver SecurityConfig). */
@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public List<MemberResponse> search(@RequestParam(required = false) String q) {
        return memberService.search(q).stream().map(MemberResponse::from).toList();
    }

    @PostMapping
    public ResponseEntity<MemberResponse> create(@Valid @RequestBody MemberRequest request) {
        var created = memberService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(MemberResponse.from(created));
    }
}
