package com.book.store.data.service;

import com.book.store.data.dto.UserRequest;
import com.book.store.data.dto.UserResponse;

import java.util.UUID;

public interface UserService {
    UserResponse create(UserRequest user);
    UserResponse update(UUID id, UserRequest user);
    void delete(UUID id);
    UserResponse findByUsername(String username);
    UserResponse findById(UUID id);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
