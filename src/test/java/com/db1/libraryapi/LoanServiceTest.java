package com.db1.libraryapi;

import com.db1.libraryapi.domain.Book;
import com.db1.libraryapi.domain.Loan;
import com.db1.libraryapi.repository.LoanRepository;
import com.db1.libraryapi.service.LoanImpl;
import com.db1.libraryapi.service.LoanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class LoanServiceTest {

    @MockBean
    private LoanRepository loanRepository;

    private LoanService loanService;

    @BeforeEach
    public void setUp() {
        this.loanService = new LoanImpl(loanRepository);

    }

    @Test
    public void deveSalvarUmEmprestimoTest() {
        Book book = Book.builder().id(1l).author("Diogo").title("As incriveis historias do homem aranha").build();
        Loan saving = Loan.builder().book(book).customer("Diogo Cortez").loanDate(LocalDate.now()).build();

        Loan savedLoan = Loan.builder().id(1l).book(book).loanDate(LocalDate.now()).customer("Diogo Cortez").build();
        when(loanRepository.save(saving)).thenReturn(savedLoan);

        Loan loan = loanService.save(saving);

        assertThat(loan.getId()).isEqualTo(savedLoan.getId());
    }

    @Test
    public void naoDeveSalvarUmEmprestimoTest() {
        Book book = Book.builder().id(1l).author("Diogo").title("As incriveis historias do homem aranha").build();
        Loan saving = Loan.builder().book(book).customer("Diogo Cortez").loanDate(LocalDate.now()).build();

        Loan savedLoan = Loan.builder().id(1l).book(book).loanDate(LocalDate.now()).customer("Diogo Cortez").build();
        when(loanRepository.save(saving)).thenReturn(savedLoan);

        Loan loan = loanService.save(saving);

        assertThat(loan.getId()).isEqualTo(savedLoan.getId());
    }
}
