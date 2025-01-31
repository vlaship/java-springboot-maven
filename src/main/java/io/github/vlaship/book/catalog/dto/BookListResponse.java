package io.github.vlaship.book.catalog.dto;

import lombok.Builder;

@Builder
public record BookListResponse(
    Long id,
    String title,
    String isbn,
    String author
) {

}
