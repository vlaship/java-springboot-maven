package com.book.store.facade.model;

import lombok.Data;

@Data
public class UpdateStoreDataRequest {
    private final String name;
    private final String address;
}
