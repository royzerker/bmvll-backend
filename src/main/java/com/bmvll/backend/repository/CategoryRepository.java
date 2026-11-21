package com.bmvll.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.bmvll.backend.model.Category;

public interface CategoryRepository extends MongoRepository<Category, String> {
    List<Category> findByActiveTrue();
    boolean existsByNameIgnoreCase(String name);

    Optional<Category> findFirstByNameIgnoreCase(String name);
    boolean existsByNameIgnoreCaseAndIdNot(String name, String id);
}