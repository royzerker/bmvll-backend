package com.bmvll.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bmvll.backend.dto.CategoryRequest;
import com.bmvll.backend.dto.CategoryResponse;
import com.bmvll.backend.service.CategoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<CategoryResponse> list() {
        return categoryService.findAll().stream().map(CategoryResponse::from).toList();
    }

    @GetMapping("/{id}")
    public CategoryResponse get(@PathVariable String id) {
        return CategoryResponse.from(categoryService.findById(id));
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> create(@Valid @RequestBody CategoryRequest request) {
        var created = categoryService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(CategoryResponse.from(created));
    }

    @PutMapping("/{id}")
    public CategoryResponse update(@PathVariable String id, @Valid @RequestBody CategoryRequest request) {
        return CategoryResponse.from(categoryService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}