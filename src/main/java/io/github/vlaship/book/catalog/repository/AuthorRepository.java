package io.github.vlaship.book.catalog.repository;

import io.github.vlaship.book.catalog.entity.Author;
import org.springframework.data.repository.CrudRepository;

public interface AuthorRepository extends CrudRepository<Author, Long> {

}
