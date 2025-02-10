package com.book.store.data.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@Table("authors")
public class Author {

  @Id
  private UUID id;
  private String name;
}
