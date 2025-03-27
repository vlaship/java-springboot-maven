package com.book.store.facade.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateBookDataRequest {
    private String title;
    private String isbn;
    private UUID authorId;
    private BookType type;
}
