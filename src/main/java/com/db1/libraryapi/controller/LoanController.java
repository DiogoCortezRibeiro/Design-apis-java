package com.db1.libraryapi.controller;

import com.db1.libraryapi.domain.Book;
import com.db1.libraryapi.domain.Loan;
import com.db1.libraryapi.dto.LoanDTO;
import com.db1.libraryapi.service.BookService;
import com.db1.libraryapi.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

@RestController
@RequestMapping("api/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;

    private final BookService bookService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long create(@RequestBody LoanDTO loanDTO) {
        Book book = bookService.getBookByAuthor(loanDTO.getAuthor()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Livro nao encontrado"));
        Loan loan = Loan.builder().book(book).customer(loanDTO.getCustomer()).loanDate(LocalDate.now()).build();

        loan = loanService.save(loan);
        return loan.getId();
    }
}
