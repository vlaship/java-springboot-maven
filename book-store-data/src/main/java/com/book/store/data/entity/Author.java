package com.book.store.data.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "authors")
public class Author {

  @Id
  private UUID id;
  private String name;
}
