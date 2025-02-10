package com.book.store.repository;

import com.book.store.entity.Book;
import java.util.List;
import org.jspecify.annotations.NonNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends CrudRepository<Book, Long> {

  @NonNull
  List<Book> findByAuthorId(@NonNull @Param("authorId") Long authorId);
}