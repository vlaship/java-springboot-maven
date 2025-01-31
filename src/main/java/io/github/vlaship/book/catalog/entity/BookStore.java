package io.github.vlaship.book.catalog.entity;

import org.springframework.data.relational.core.mapping.Table;

@Table("book_stores")
public record BookStore(Long bookId, Long storeId) {

}
