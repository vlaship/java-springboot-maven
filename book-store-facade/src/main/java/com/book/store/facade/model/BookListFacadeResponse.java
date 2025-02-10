package com.book.store.facade.model;

import lombok.Data;

import java.util.UUID;

@Data
public class BookListFacadeResponse {
    private final UUID id;
    private final String title;
    private final String isbn;
    private final String author;
}
