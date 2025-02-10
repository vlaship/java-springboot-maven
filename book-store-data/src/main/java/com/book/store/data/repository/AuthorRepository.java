package com.book.store.data.repository;

import com.book.store.data.entity.Author;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface AuthorRepository extends CrudRepository<Author, UUID> {

}
