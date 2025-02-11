package com.book.store.facade.model;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class BookDataResponse {
    private UUID id;
    private String title;
    private String isbn;
    private BookType type;
    private UUID authorId;
    private List<UUID> storeIds;
}
