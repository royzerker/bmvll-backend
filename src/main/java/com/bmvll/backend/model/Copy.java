package com.bmvll.backend.model;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("copies")
@CompoundIndex(name = "book_status", def = "{'bookId': 1, 'status': 1}")
public class Copy {

    @Id
    private String id;

    @Indexed
    private String bookId;

    @Indexed(unique = true)
    private String inventoryCode;

    @Indexed
    private CopyStatus status;

    private CopyCondition condition;
    private Location location;
    private Instant acquisitionDate;
}