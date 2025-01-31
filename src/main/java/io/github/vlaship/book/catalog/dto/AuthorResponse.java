package io.github.vlaship.book.catalog.dto;

import lombok.Builder;

@Builder
public record AuthorResponse(Long id, String name) {

}
