package com.book.store.entity;

import lombok.Data;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@Table("book_stores")
public class BookStore {

    private UUID bookId;
    private UUID storeId;
}
