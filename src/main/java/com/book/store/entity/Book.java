package com.book.store.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@Table("books")
public class Book {

  @Id
  private Long id;
  private String title;
  private String isbn;
  private Long authorId;
}
