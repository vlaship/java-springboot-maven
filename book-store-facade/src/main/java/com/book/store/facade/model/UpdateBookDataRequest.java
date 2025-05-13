package com.book.store.facade.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBookDataRequest {
    private String title;
    private String isbn;
    private BookType type;
    private UUID authorId;
    private List<UUID> storeIds;
}
