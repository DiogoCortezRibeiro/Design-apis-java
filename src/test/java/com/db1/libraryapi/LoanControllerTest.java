package com.db1.libraryapi;

import com.db1.libraryapi.controller.LoanController;
import com.db1.libraryapi.domain.Book;
import com.db1.libraryapi.domain.Loan;
import com.db1.libraryapi.dto.LoanDTO;
import com.db1.libraryapi.service.BookService;
import com.db1.libraryapi.service.LoanService;
import com.db1.libraryapi.utils.BusinessException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
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

import java.time.LocalDate;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = LoanController.class)
@AutoConfigureMockMvc
public class LoanControllerTest {

    static final String LOAN_API = "/api/loans";

    @Autowired
    MockMvc mvc;

    @MockBean
    BookService bookService;

    @MockBean
    LoanService loanService;

    @Test
    public void deveRealizarUmEmprestimoTest() throws Exception{
        // cenario
        LoanDTO dto = LoanDTO.builder().author("Diogo").customer("Diogo Cortez").build();
        String json = new ObjectMapper().writeValueAsString(dto);

        Book book = Book.builder().id(1l).author("Diogo").title("Harry potter").build();
        BDDMockito.given(bookService.getBookByAuthor("Diogo")).willReturn(Optional.of(book));

        Loan loan = Loan.builder().id(1l).customer("Diogo Cortez").book(book).loanDate(LocalDate.now()).build();
        BDDMockito.given(loanService.save(Mockito.any(Loan.class))).willReturn(loan);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(LOAN_API)
                .accept(MediaType.APPLICATION_JSON)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON);

        mvc.perform(request).andExpect(status().isCreated()).andExpect(content().string("1"));
    }

    @Test
    public void deveLancarExcecaoParaLivroInexistenteTest() throws Exception {
        LoanDTO dto = LoanDTO.builder().author("Diogo").customer("Diogo Cortez").build();
        String json = new ObjectMapper().writeValueAsString(dto);

        BDDMockito.given(bookService.getBookByAuthor("Diogo")).willReturn(Optional.empty());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(LOAN_API)
                .accept(MediaType.APPLICATION_JSON)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON);

        mvc.perform(request).andExpect(status().isBadRequest());
    }
}
