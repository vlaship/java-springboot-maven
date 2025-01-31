package io.github.vlaship.book.catalog.dto;

import lombok.Builder;

@Builder
public record StoreResponse(Long id, String name, String address) {

}
