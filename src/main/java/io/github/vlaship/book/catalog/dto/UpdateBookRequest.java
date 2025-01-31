package io.github.vlaship.book.catalog.dto;

import lombok.Builder;

@Builder
public record UpdateBookRequest(
    String title,
    String isbn,
    Long authorId
) {

}
