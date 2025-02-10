package com.book.store.dto;

import lombok.Builder;

@Builder
public record AuthorResponse(Long id, String name) {

}
