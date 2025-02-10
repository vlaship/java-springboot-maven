package com.book.store.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class BookResponse {
    private final UUID id;
    private final String title;
    private final String isbn;
    private final UUID authorId;
    private final List<UUID> storeIds;
}
