package com.db1.libraryapi;

import com.db1.libraryapi.domain.Book;
import com.db1.libraryapi.dto.BookDTO;
import com.db1.libraryapi.service.BookService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

// spirng deve criar um mini contexto para rodar o test
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest
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
    public void naoDeveCriarUmNovoLivroTest() {

    }
}
