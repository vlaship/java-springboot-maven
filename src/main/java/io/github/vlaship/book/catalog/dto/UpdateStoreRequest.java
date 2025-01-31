package io.github.vlaship.book.catalog.dto;

import lombok.Builder;

@Builder
public record UpdateStoreRequest(String name, String address) {

}
