package com.db1.libraryapi.repository;

import com.db1.libraryapi.domain.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepository extends JpaRepository<Loan, Long> {
}
