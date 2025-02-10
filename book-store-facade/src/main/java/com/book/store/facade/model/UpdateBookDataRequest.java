package com.book.store.facade.model;

import lombok.Data;

import java.util.UUID;

@Data
public class UpdateBookDataRequest {
    private final String title;
    private final String isbn;
    private final BookType type;
    private final UUID authorId;
}
