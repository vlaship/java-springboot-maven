package io.github.vlaship.book.catalog.dto;

import lombok.Builder;

@Builder
public record CreateStoreRequest(String name, String address) {

}
