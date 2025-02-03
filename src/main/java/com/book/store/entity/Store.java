package com.book.store.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@Table("stores")
public class Store {

  @Id
  private Long id;
  private String name;
  private String address;
}
