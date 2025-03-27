package com.book.store.data.repository;

import com.book.store.data.entity.Book;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends JpaRepository<Book, UUID> {

    @Query("FROM Book b WHERE b.author.id = :authorId")
    List<Book> findAllByAuthorId(@Param("authorId") UUID authorId);
}
