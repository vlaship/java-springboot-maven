package com.book.store.data.dto;

import com.book.store.data.entity.BookType;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;
import org.hibernate.validator.constraints.ISBN;

import java.util.List;
import java.util.UUID;

@Data
public class CreateBookRequest {
    @Size(min = 2, max = 50)
    private final String title;
    @ISBN
    private final String isbn;
    @NotNull
    private final BookType type;
    @NotNull
    private final UUID authorId;
    @NotNull
    @Size(min = 1)
    private final List<UUID> storeIds;
}
