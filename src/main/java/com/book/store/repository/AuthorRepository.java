package com.book.store.repository;

import com.book.store.entity.Author;
import org.springframework.data.repository.CrudRepository;

public interface AuthorRepository extends CrudRepository<Author, Long> {

}
