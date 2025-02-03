package com.book.store.dto;

import lombok.Builder;

@Builder
public record StoreResponse(Long id, String name, String address) {

}
