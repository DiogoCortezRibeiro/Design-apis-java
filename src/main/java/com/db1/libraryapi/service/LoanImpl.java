package com.db1.libraryapi.service;

import com.db1.libraryapi.domain.Loan;
import com.db1.libraryapi.repository.LoanRepository;
import org.springframework.stereotype.Service;

@Service
public class LoanImpl implements LoanService {

    private LoanRepository loanRepository;

    public LoanImpl(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    @Override
    public Loan save(Loan loan) {
        return loanRepository.save(loan);
    }
}
