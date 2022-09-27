package com.db1.libraryapi;

import com.db1.libraryapi.domain.Book;
import com.db1.libraryapi.domain.Loan;
import com.db1.libraryapi.repository.LoanRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class LoanRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private LoanRepository loanRepository;

    @Test
    public void deveVerificarSeExisteEmprestimoNaoDevolvidoParaOLivro() {
        // cenario
        Book book = Book.builder().author("Diogo").title("As incriveis historias do homem aranha").build();
        entityManager.persist(book);

        Loan loan = Loan.builder().book(book).customer("Diogo Cortez").loanDate(LocalDate.now()).build();
        entityManager.persist(loan);

        // execucao
        Boolean exists = loanRepository.existsByBookAndNotReturned(book);

        assertThat(exists).isTrue();
    }

    @Test
    public void deveObterEmprestimosCujaDataEmprestimoMenorOuIgualATresDiasAtras() {
        Book book = Book.builder().author("Diogo").title("As incriveis historias do homem aranha").build();
        entityManager.persist(book);

        Loan loan = Loan.builder().book(book).customer("Diogo Cortez").loanDate(LocalDate.now().minusDays(5)).build();
        entityManager.persist(loan);

        List<Loan> result = loanRepository.findByLoanDateLessThanAndNotReturned(LocalDate.now().minusDays(4));

        assertThat(result).hasSize(1).contains(loan);
    }
}
