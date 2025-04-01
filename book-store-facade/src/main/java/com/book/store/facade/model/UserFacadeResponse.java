package com.book.store.facade.model;

import lombok.Data;

import java.util.Collection;
import java.util.UUID;

@Data
public class UserFacadeResponse {
    private final UUID id;
    private final String username;
    private final String email;
    private final Collection<String> roles;
}
