package com.db1.libraryapi.service;

import com.db1.libraryapi.domain.Book;
import com.db1.libraryapi.domain.Loan;
import com.db1.libraryapi.repository.LoanRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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

    @Override
    public Optional<Loan> getById(Long id) {
        return loanRepository.findById(id);
    }

    @Override
    public Loan update(Loan loan) {
        loanRepository.save(loan);
        return loan;
    }

    @Override
    public Page<Loan> getLoansByBook(Book book, Pageable pageable) {
        return loanRepository.findByBook(book, pageable);
    }

    @Override
    public List<Loan> getAllLateLoans() {
        final Integer loanDays = 4;
        LocalDate thereDaysAgora = LocalDate.now().minusDays(loanDays);
        return loanRepository.findByLoanDateLessThanAndNotReturned(thereDaysAgora);
    }
}
