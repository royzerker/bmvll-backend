package com.bmvll.backend.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.bmvll.backend.dto.CategoryRequest;
import com.bmvll.backend.model.Category;
import com.bmvll.backend.repository.CategoryRepository;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /** Solo categorías activas (soft delete, ver docs/MODELADO.md). */
    public List<Category> findAll() {
        return categoryRepository.findByActiveTrue();
    }

    public Category findById(String id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoría no encontrada"));
    }

    public Category create(CategoryRequest request) {
        String name = request.name().trim();
        if (categoryRepository.existsByNameIgnoreCase(name)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe una categoría con ese nombre");
        }

        Category category = new Category();
        category.setName(name);
        category.setDescription(request.description());
        category.setActive(true);

        return categoryRepository.save(category);
    }

    public Category update(String id, CategoryRequest request) {
        Category category = findById(id);

        String name = request.name().trim();
        if (categoryRepository.existsByNameIgnoreCaseAndIdNot(name, id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe una categoría con ese nombre");
        }

        category.setName(name);
        category.setDescription(request.description());

        return categoryRepository.save(category);
    }

    /** Baja lógica: no se borra el documento, solo se marca active = false. */
    public void delete(String id) {
        Category category = findById(id);
        category.setActive(false);
        categoryRepository.save(category);
    }
}