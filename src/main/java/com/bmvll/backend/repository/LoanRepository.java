package com.bmvll.backend.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.bmvll.backend.model.Loan;
import com.bmvll.backend.model.LoanStatus;

public interface LoanRepository extends MongoRepository<Loan, String> {

    List<Loan> findByUserIdOrderByLoanDateDesc(String userId);

    List<Loan> findByStatusOrderByDueDateAsc(LoanStatus status);
}
