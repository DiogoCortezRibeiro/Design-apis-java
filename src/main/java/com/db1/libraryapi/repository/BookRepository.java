package com.db1.libraryapi.repository;

import com.db1.libraryapi.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
