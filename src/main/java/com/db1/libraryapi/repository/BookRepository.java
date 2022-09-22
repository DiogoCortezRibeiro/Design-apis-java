package com.db1.libraryapi.repository;

import com.db1.libraryapi.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    boolean existsByTitle(String title);

    Optional<Book> findByAuthor(String author);
}
