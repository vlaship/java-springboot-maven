package com.book.store.facade.model;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class BookFacadeResponse {
    private final UUID id;
    private final String title;
    private final String isbn;
    private final BookType type;
    private final UUID authorId;
    private final List<UUID> storeIds;
}
