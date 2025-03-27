package com.book.store.facade.model;

import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.ISBN;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBookFacadeRequest {
    @Size(min = 2, max = 50)
    private String title;
    @ISBN
    private String isbn;
    private BookType type;
    private UUID authorId;
}
