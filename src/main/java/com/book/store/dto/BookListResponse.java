package com.book.store.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class BookListResponse {
    private final UUID id;
    private final String title;
    private final String isbn;
    private final String author;
}
