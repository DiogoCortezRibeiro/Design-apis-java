package com.db1.libraryapi;

import com.db1.libraryapi.controller.BookController;
import com.db1.libraryapi.domain.Book;
import com.db1.libraryapi.dto.BookDTO;
import com.db1.libraryapi.service.BookService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.Optional;

// spirng deve criar um mini contexto para rodar o test
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = BookController.class)
@AutoConfigureMockMvc
public class BookControllerTest {

    static String BOOK_API = "/api/books";

    @Autowired
    MockMvc mvc;

    @MockBean
    BookService service;

    @Test
    public void deveCriarUmNovoLivroTest() throws Exception {
        BookDTO dto = BookDTO.builder().author("Diogo").title("A aventuras").build();
        BDDMockito.given(service.save(Mockito.any(Book.class))).willReturn(Book.builder().id(10L).author("Diogo").title("A aventuras").build());
        String json = new ObjectMapper().writeValueAsString(dto);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(BOOK_API).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(json);

        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("author").value(dto.getAuthor()))
                .andExpect(MockMvcResultMatchers.jsonPath("title").value(dto.getTitle()));
    }

    @Test
    public void naoDeveCriarUmNovoLivroTest() throws Exception {
        String json = new ObjectMapper().writeValueAsString(new BookDTO());
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(BOOK_API).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(json);
        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("erros", Matchers.hasSize(2)));
    }

    @Test
    public void deveObterInformacoesDeUmLivro() throws Exception {
        // cenario
        Long id = 1L;
        Book book = Book.builder().title("Harry potter").author("Diogo").id(id).build();
        BDDMockito.given(service.getById(id)).willReturn(Optional.of(book));

        // execução
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(BOOK_API.concat("/" + id)).accept(MediaType.APPLICATION_JSON);

        // verificação
        mvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk())
                            .andExpect(MockMvcResultMatchers.jsonPath("id").value(id))
                            .andExpect(MockMvcResultMatchers.jsonPath("author").value(book.getAuthor()))
                            .andExpect(MockMvcResultMatchers.jsonPath("title").value(book.getTitle()));
    }

    @Test
    public void naoDeveEncontrarLivro() throws Exception {
        // cenario
        BDDMockito.given(service.getById(Mockito.anyLong())).willReturn(Optional.empty());
        // execução
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(BOOK_API.concat("/" + 1)).accept(MediaType.APPLICATION_JSON);
        // verificação
        mvc.perform(request).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void deveDeletarUmLivro() throws Exception {
        // cenario
        BDDMockito.given(service.getById(Mockito.anyLong())).willReturn(Optional.of(Book.builder().id(1L).build()));

        // execução
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.delete(BOOK_API.concat("/" + 1));

        // verificação
        mvc.perform(request).andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void deveAtualizarUmLivro() throws Exception {
        Long id = 1l;
        String json = new ObjectMapper().writeValueAsString(Book.builder().title("Harry potter - calices de fogo").author("Diogo").id(id).build());

        // cenario
        BDDMockito.given(service.getById(id)).willReturn(Optional.of(Book.builder().id(id).title("Harry potter - calices de fogo").author("Diogo").build()));

        // execução
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put(BOOK_API.concat("/" + id))
                                                                      .content(json).accept(MediaType.APPLICATION_JSON)
                                                                      .contentType(MediaType.APPLICATION_JSON);

        // verificação
        mvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk())
                            .andExpect(MockMvcResultMatchers.jsonPath("id").value(id))
                            .andExpect(MockMvcResultMatchers.jsonPath("title").value("Harry potter - calices de fogo"))
                            .andExpect(MockMvcResultMatchers.jsonPath("author").value("Diogo"));
    }

    @Test
    public void naoDeveAtualizarUmLivro() throws Exception {
        String json = new ObjectMapper().writeValueAsString(Book.builder().title("Harry potter").author("Diogo").id(10l).build());

        // cenario
        BDDMockito.given(service.getById(Mockito.anyLong())).willReturn(Optional.empty());

        // execução
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put(BOOK_API.concat("/" + 1))
                .content(json).accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        // verificação
        mvc.perform(request).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void deveFiltrarLivros() throws Exception {
        Long id = 1l;
        Book book = Book.builder().title("Harry potter - calices de fogo").author("Diogo").id(id).build();

        BDDMockito.given(service.find(Mockito.any(Book.class), Mockito.any(Pageable.class)))
                  .willReturn(new PageImpl<Book>(Arrays.asList(book), PageRequest.of(0, 100), 1 ));

        String queryString = String.format("?title=%s&author=%s&page=0&size=100", book.getTitle(), book.getAuthor());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(BOOK_API.concat(queryString)).accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON);

        mvc.perform(request)
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.jsonPath("content", Matchers.hasSize(1)))
               .andExpect(MockMvcResultMatchers.jsonPath("totalElements").value(1)) ;
    }
}
