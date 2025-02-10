package com.book.store.facade.model;

import lombok.Data;

import java.util.UUID;

@Data
public class CreateBookDataRequest {
    private final String title;
    private final String isbn;
    private final UUID authorId;
    private final BookType type;
}
