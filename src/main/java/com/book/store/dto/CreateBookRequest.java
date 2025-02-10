package com.book.store.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.ISBN;

import java.util.UUID;

@Data
public class CreateBookRequest {
    @Size(min = 2, max = 50)
    private final String title;
    @ISBN
    private final String isbn;
    @NotNull
    private final UUID authorId;
}
