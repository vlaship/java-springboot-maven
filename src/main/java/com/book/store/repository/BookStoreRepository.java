package com.book.store.repository;

import com.book.store.entity.BookStore;

import java.util.List;
import java.util.UUID;

import org.jspecify.annotations.NonNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface BookStoreRepository extends CrudRepository<BookStore, UUID> {

    @NonNull
    List<BookStore> findByBookId(@NonNull @Param("bookId") UUID bookId);
}