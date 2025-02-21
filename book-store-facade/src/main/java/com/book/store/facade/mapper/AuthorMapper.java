package com.book.store.facade.mapper;

import com.book.store.facade.model.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class AuthorMapper {

    public AuthorFacadeResponse map(AuthorDataResponse data) {
        if (data == null) {
            return null;
        }

        UUID id = data.getId();
        String name = data.getName();

        return new AuthorFacadeResponse(id, name);
    }

    public List<AuthorFacadeResponse> map(List<AuthorDataResponse> data) {
        if (data == null) {
            return null;
        }

        List<AuthorFacadeResponse> list = new ArrayList<>(data.size());
        for (AuthorDataResponse authorDataResponse : data) {
            list.add(map(authorDataResponse));
        }

        return list;
    }

    public CreateAuthorDataRequest map(CreateAuthorFacadeRequest facade) {
        if (facade == null) {
            return null;
        }

        String name = facade.getName();

        return new CreateAuthorDataRequest(name);
    }

    public UpdateAuthorDataRequest map(UpdateAuthorFacadeRequest facade) {
        if (facade == null) {
            return null;
        }

        String name = facade.getName();

        return new UpdateAuthorDataRequest(name);
    }
}
