package com.bmvll.backend.dto;

import java.time.Instant;

import com.bmvll.backend.model.Loan;
import com.bmvll.backend.model.LoanStatus;

public record LoanResponse(
        String id,
        String copyId,
        String bookId,
        String userId,
        String librarianId,
        Instant loanDate,
        Instant dueDate,
        Instant returnDate,
        LoanStatus status,
        String notes) {

    /** OVERDUE no se guarda: se calcula al consultar (ACTIVE con dueDate vencida). */
    public static LoanResponse from(Loan loan, Instant now) {
        LoanStatus status = loan.getStatus() == LoanStatus.ACTIVE && loan.getDueDate().isBefore(now)
                ? LoanStatus.OVERDUE
                : loan.getStatus();
        return new LoanResponse(
                loan.getId(),
                loan.getCopyId(),
                loan.getBookId(),
                loan.getUserId(),
                loan.getLibrarianId(),
                loan.getLoanDate(),
                loan.getDueDate(),
                loan.getReturnDate(),
                status,
                loan.getNotes());
    }
}
