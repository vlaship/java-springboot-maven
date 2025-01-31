package io.github.vlaship.book.catalog.dto;

import lombok.Builder;

@Builder
public record CreateBookRequest(
    String title,
    String isbn,
    Long authorId
) {

}
