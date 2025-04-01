package com.book.store.facade.model;

import lombok.Data;

import java.util.Collection;
import java.util.UUID;

@Data
public class UserDataResponse {
    private UUID id;
    private String username;
    private String password;
    private String email;
    private Collection<String> roles;
}