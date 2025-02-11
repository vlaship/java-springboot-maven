package com.book.store.data.dto;

import com.book.store.data.entity.BookType;
import javax.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.ISBN;

import java.util.List;
import java.util.UUID;

@Data
public class UpdateBookRequest {
    @Size(min = 2, max = 50)
    private final String title;
    @ISBN
    private final String isbn;
    private final BookType type;
    private final UUID authorId;
    private final List<UUID> storeIds;
}
