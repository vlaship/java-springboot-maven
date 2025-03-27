package com.book.store.data.dto;

import com.book.store.data.entity.BookType;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.ISBN;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateBookRequest {
    @Size(min = 2, max = 50)
    private String title;
    @ISBN
    private String isbn;
    @NotNull
    private BookType type;
    @NotNull
    private UUID authorId;
    @NotNull
    @Size(min = 1)
    private List<UUID> storeIds;
}
