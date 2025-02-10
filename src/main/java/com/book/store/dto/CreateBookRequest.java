package com.book.store.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.hibernate.validator.constraints.ISBN;

@Builder
public record CreateBookRequest(
    @NotBlank @Size(min = 2, max = 50)
    String title,
    @NotBlank @ISBN
    String isbn,
    @NotNull @Positive
    Long authorId
) {

}
