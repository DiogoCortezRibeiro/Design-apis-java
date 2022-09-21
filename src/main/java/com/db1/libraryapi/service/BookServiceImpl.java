package com.db1.libraryapi.service;

import com.db1.libraryapi.domain.Book;
import com.db1.libraryapi.repository.BookRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BookServiceImpl implements BookService{

    private BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Book save(Book book) {
        if(bookRepository.existsByTitle(book.getTitle())) {
            throw new RuntimeException("Livro já cadastrado");
        }
        return bookRepository.save(book);
    }

    @Override
    public Optional<Book> getById(Long id) {
        return bookRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        Book book = bookRepository.getById(id);
        bookRepository.delete(book);
    }

    @Override
    public void delete(Book book) {
        if(book.getId() == null) {
            throw new IllegalArgumentException("ID do livro está null");
        }

        this.bookRepository.delete(book);
    }

    @Override
    public Book update(Book book) {
        if(book.getId() == null) {
            throw new IllegalArgumentException("ID do livro está null");
        }
        return bookRepository.save(book);
    }

    @Override
    public Page<Book> find(Book book, Pageable page) {
        Example<Book> example = Example.of(book, ExampleMatcher.matching().withIgnoreCase().withIgnoreNullValues().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING));
        return bookRepository.findAll(example, page);
    }
}
