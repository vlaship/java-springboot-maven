package com.book.store.data.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class StoreResponse {
    private final UUID id;
    private final String name;
    private final String address;
}
