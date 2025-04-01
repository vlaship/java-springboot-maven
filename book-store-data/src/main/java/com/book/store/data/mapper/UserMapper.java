package com.book.store.data.mapper;

import com.book.store.data.dto.UserRequest;
import com.book.store.data.dto.UserResponse;
import com.book.store.data.entity.User;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

@Component
public class UserMapper {

    public UserResponse map(User entity) {
        if (entity == null) {
            return null;
        }

        UUID id = entity.getId();
        String username = entity.getUsername();
        String password = entity.getPassword();
        String email = entity.getEmail();
        Collection<String> roles = Arrays.asList(entity.getRoles().split(","));

        return new UserResponse(id, username, password, email, roles);
    }

    public User map(UserRequest dto) {
        if (dto == null) {
            return null;
        }

        return map(UUID.randomUUID(), dto);
    }

    public User map(UUID id, UserRequest dto) {
        if (dto == null) {
            return null;
        }

        String roles = String.join(",", dto.getRoles());

        return User.builder()
                .id(id)
                .username(dto.getUsername())
                .password(dto.getPassword())
                .email(dto.getEmail())
                .roles(roles)
                .build();
    }
}
