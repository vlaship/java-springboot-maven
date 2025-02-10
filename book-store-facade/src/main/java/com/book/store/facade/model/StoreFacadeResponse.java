package com.book.store.facade.model;

import lombok.Data;

import java.util.UUID;

@Data
public class StoreFacadeResponse {
    private final UUID id;
    private final String name;
    private final String address;
}
