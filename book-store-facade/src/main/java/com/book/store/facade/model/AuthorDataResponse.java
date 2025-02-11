package com.book.store.facade.model;

import lombok.Data;

import java.util.UUID;

@Data
public class AuthorDataResponse {
    private UUID id;
    private String name;
}
