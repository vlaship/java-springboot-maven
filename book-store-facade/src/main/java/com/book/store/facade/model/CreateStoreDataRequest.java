package com.book.store.facade.model;

import lombok.Data;

@Data
public class CreateStoreDataRequest {
    private final String name;
    private final String address;
}
