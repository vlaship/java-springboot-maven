package com.book.store.entity;

import lombok.Data;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("book_stores")
public class BookStore {

  private Long bookId;
  private Long storeId;
}
