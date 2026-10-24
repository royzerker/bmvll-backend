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
@Document("loans")
@CompoundIndex(name = "user_status", def = "{'userId': 1, 'status': 1}")
public class Loan {

    @Id
    private String id;

    @Indexed
    private String copyId;

    private String bookId;

    @Indexed
    private String userId;

    private String librarianId;

    private Instant loanDate;
    private Instant dueDate;
    private Instant returnDate;

    @Indexed
    private LoanStatus status;

    private String notes;
}
