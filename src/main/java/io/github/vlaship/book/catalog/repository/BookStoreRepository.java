package io.github.vlaship.book.catalog.repository;

import io.github.vlaship.book.catalog.entity.BookStore;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface BookStoreRepository extends CrudRepository<BookStore, Long> {

  List<BookStore> findByBookId(Long bookId);
}