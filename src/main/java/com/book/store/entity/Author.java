package com.book.store.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@Table("authors")
public class Author {

  @Id
  private Long id;
  private String name;
}
