package com.bmvll.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.bmvll.backend.model.Copy;
import com.bmvll.backend.model.CopyStatus;

public interface CopyRepository extends MongoRepository<Copy, String> {

    Optional<Copy> findByInventoryCode(String inventoryCode);

    boolean existsByInventoryCode(String inventoryCode);

    List<Copy> findByBookId(String bookId);

    List<Copy> findByBookIdAndStatus(String bookId, CopyStatus status);

    long countByBookIdAndStatus(String bookId, CopyStatus status);

    long countByBookId(String bookId);
}