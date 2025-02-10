package com.book.store.data.dto;

import com.book.store.data.entity.BookType;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class BookResponse {
    private final UUID id;
    private final String title;
    private final String isbn;
    private final BookType type;
    private final UUID authorId;
    private final List<UUID> storeIds;
}
