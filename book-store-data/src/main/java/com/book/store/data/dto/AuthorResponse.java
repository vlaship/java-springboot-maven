package com.book.store.data.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class AuthorResponse {
    private final UUID id;
    private final String name;
}
