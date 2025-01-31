package io.github.vlaship.book.catalog.repository;

import io.github.vlaship.book.catalog.entity.Book;
import org.springframework.data.repository.CrudRepository;

public interface BookRepository extends CrudRepository<Book, Long> {

}