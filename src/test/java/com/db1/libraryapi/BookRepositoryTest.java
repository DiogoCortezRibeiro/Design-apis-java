package com.db1.libraryapi;

import com.db1.libraryapi.domain.Book;
import com.db1.libraryapi.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class BookRepositoryTest {

    // objeto par criar cenaroo, simula entity manager
    @Autowired
    TestEntityManager entityManager;

    @Autowired
    BookRepository bookRepository;

    @Test
    public void deveRetornarVerdadeiroQuandoExistirUmLivroNaBaseComTituloInformado() {
        // cenario
        String titulo = "Harry Potter";
        Book book = Book.builder().title(titulo).author("Diogo").build();
        entityManager.persist(book);

        // execução
        boolean exists = bookRepository.existsByTitle(titulo);

        // verificação
        assertThat(exists).isTrue();
    }

    @Test
    public void deveObterUmLivroPorId() {
        // cenario
        Book book = Book.builder().title("Harry Potter").author("Diogo").id(1l).build();
        entityManager.persist(book);

        // execução
        Optional<Book> foundBook = bookRepository.findById(book.getId());

        // verificação
        assertThat(foundBook.isPresent()).isTrue();
    }

    @Test
    public void deveSalvarUmlivro() {
        Book book = Book.builder().title("Harry Potter").author("Diogo").build();

        Book bookSalvo = bookRepository.save(book);

        assertThat(bookSalvo.getId()).isNotNull();
    }

    @Test
    public void deveExcluirUmLivro() {
        Book book = Book.builder().title("Harry Potter").author("Diogo").build();
        entityManager.persist(book);

        Book foundBook = entityManager.find(Book.class, book.getId());

        bookRepository.delete(foundBook);

        Book deletedBook = entityManager.find(Book.class, book.getId());

        assertThat(deletedBook).isNull();
    }

}
