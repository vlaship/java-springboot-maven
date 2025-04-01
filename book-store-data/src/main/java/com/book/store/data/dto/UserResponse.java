package com.book.store.data.dto;

import lombok.Data;

import java.util.Collection;
import java.util.UUID;

@Data
public class UserResponse {
    private final UUID id;
    private final String username;
    private final String password;
    private final String email;
    private final Collection<String> roles;
}