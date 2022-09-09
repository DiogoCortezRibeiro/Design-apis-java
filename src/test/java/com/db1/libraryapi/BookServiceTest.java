package com.db1.libraryapi;

import com.db1.libraryapi.domain.Book;
import com.db1.libraryapi.repository.BookRepository;
import com.db1.libraryapi.service.BookService;
import com.db1.libraryapi.service.BookServiceImpl;
import org.aspectj.lang.annotation.Before;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class BookServiceTest {

    BookService service;

    @MockBean
    BookRepository bookRepository;

    @BeforeEach
    public void setUp() {
        this.service = new BookServiceImpl(bookRepository);
    }

    @Test
    public void deveSalvarUmLivroTest() {
        // cenario
        Book book = Book.builder().title("Harry potter").author("Diogo").build();
        Mockito.when(bookRepository.save(book)).thenReturn(Book.builder().id(1L).title("Harry potter").author("Diogo").build());

        // execução
        Book saveBook = service.save(book);

        // verificação
        Assertions.assertThat(saveBook.getId()).isNotNull();
        Assertions.assertThat(saveBook.getAuthor()).isEqualTo("Diogo");
        Assertions.assertThat(saveBook.getTitle()).isEqualTo("Harry potter");
    }
}
