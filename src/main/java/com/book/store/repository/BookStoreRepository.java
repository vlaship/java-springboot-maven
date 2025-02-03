package com.book.store.repository;

import com.book.store.entity.BookStore;
import java.util.List;
import org.jspecify.annotations.NonNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface BookStoreRepository extends CrudRepository<BookStore, Long> {

  @NonNull
  List<BookStore> findByBookId(@NonNull @Param("bookId") Long bookId);
}