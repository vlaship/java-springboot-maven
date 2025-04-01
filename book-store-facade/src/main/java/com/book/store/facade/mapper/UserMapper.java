package com.book.store.facade.mapper;

import com.book.store.facade.model.*;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.UUID;

@Component
public class UserMapper {

    public User toModel(UserDataResponse data) {
        if (data == null) {
            return null;
        }

        UUID id = data.getId();
        String username = data.getUsername();
        String password = data.getPassword();
        String email = data.getEmail();
        Collection<String> roles = data.getRoles();

        return new User(id, username, password, email, roles);
    }

    public CreateUserDataRequest map(CreateUserFacadeRequest facade) {
        if (facade == null) {
            return null;
        }

        String username = facade.getUsername();
        String password = facade.getPassword();
        String email = facade.getEmail();
        Collection<String> roles = facade.getRoles();

        return new CreateUserDataRequest(username, password, email, roles);
    }

    public UpdateUserDataRequest map(UpdateUserFacadeRequest facade) {
        if (facade == null) {
            return null;
        }

        String username = facade.getUsername();
        String password = facade.getPassword();
        String email = facade.getEmail();
        Collection<String> roles = facade.getRoles();

        return new UpdateUserDataRequest(username, password, email, roles);
    }

    public UserFacadeResponse map(UserDataResponse data) {
        if (data == null) {
            return null;
        }

        UUID id = data.getId();
        String username = data.getUsername();
        String email = data.getEmail();
        Collection<String> roles = data.getRoles();

        return new UserFacadeResponse(id, username, email, roles);
    }
}
