package com.book.store.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@Table("books")
public class Book {

    @Id
    private UUID id;
    private String title;
    private String isbn;
    private UUID authorId;
}
