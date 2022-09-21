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
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

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

        Mockito.when(bookRepository.existsByTitle(Mockito.anyString())).thenReturn(false);

        // execução
        Book saveBook = service.save(book);

        // verificação
        Assertions.assertThat(saveBook.getId()).isNotNull();
        Assertions.assertThat(saveBook.getAuthor()).isEqualTo("Diogo");
        Assertions.assertThat(saveBook.getTitle()).isEqualTo("Harry potter");
    }

    @Test
    public void deveLancarErroDeNegocioAoTentarSalvarLivroComTituloDuplicado() {
        // cenario
        Book book = Book.builder().title("Harry potter").author("Diogo").build();
        Mockito.when(bookRepository.existsByTitle(Mockito.anyString())).thenReturn(true);

        // execução
        Throwable ex = Assertions.catchThrowable( () -> service.save(book) );

        // verificações
        assertThat(ex).isInstanceOf(RuntimeException.class).hasMessage("Livro já cadastrado");

        // em caso de erro, verifica que nunca será chamado o metodo salvar
        Mockito.verify(bookRepository, Mockito.never()).save(book);
    }

    @Test
    public void deveObterUmLivroPorId() {
        Long id = 1l;
        Book book = Book.builder().id(id).title("Harry potter").author("Diogo").build();
        Mockito.when(bookRepository.findById(id)).thenReturn(Optional.of(book));

        // execução
        Optional<Book> foundBook = service.getById(id);

        // verificações
        assertThat(foundBook.isPresent()).isTrue();
        assertThat(foundBook.get().getId()).isEqualTo(id);
    }

    @Test
    public void deveDeletarUmLivro() {
        Long id = 1l;
        Book book = Book.builder().id(id).title("Harry Potter").author("Diogo").build();

        // execução.
        service.delete(book);

        // verificacao
        Mockito.verify(bookRepository, Mockito.times(1)).delete(book);
    }

    @Test
    public void naoDeveDeletarUmlivro() {
        Book book = new Book();

        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> service.delete(book));
        Mockito.verify(bookRepository, Mockito.never()).delete(book);
    }

    @Test
    public void deveAtualizarUmLivroTest() {
        Long id = 1l;
        Book book = Book.builder().id(id).title("Harry Potter").author("Diogo").build();
        Mockito.when(bookRepository.save(book)).thenReturn(Book.builder().id(1L).title("Harry potter e o calice de fogo").author("Diogo").build());

        Book bookAtualizado = service.update(book);

        // verificação
        assertThat(bookAtualizado.getTitle()).isEqualTo("Harry potter e o calice de fogo");
    }

    @Test
    public void naoDeveAtualizarUmLivroTest() {
        Book book = Book.builder().title("Harry Potter").author("Diogo").build();

        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> service.update(book));
        Mockito.verify(bookRepository, Mockito.never()).save(book);
    }

    @Test
    public void naoDeveObterUmLivroPorIdTest() {
        Mockito.when(bookRepository.findById(1l)).thenReturn(Optional.empty());

        // execução
        Optional<Book> foundBook = service.getById(1l);

        // verificações
        assertThat(foundBook.isPresent()).isFalse();
    }

    @Test
    public void deveFiltrarLivroTest() {
        Book book = Book.builder().title("Harry Potter").author("Diogo").id(1l).build();
        Page<Book> page = new PageImpl<Book>(Arrays.asList(book), PageRequest.of(0, 10), 1);
        Mockito.when(bookRepository.findAll(Mockito.any(Example.class), Mockito.any(PageRequest.class))).thenReturn(page);

        Page<Book> result = service.find(book, PageRequest.of(0, 10));

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent()).isEqualTo(Arrays.asList(book));
    }
}
