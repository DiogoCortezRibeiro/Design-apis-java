package com.db1.libraryapi.service;

import com.db1.libraryapi.domain.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BookService {

    Book save(Book book);

    Optional<Book> getById(Long id);

    void deleteById(Long id);

    Book update(Book book);

    void delete(Book book);

    Page<Book> find(Book book, Pageable page);
}
