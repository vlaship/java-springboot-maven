package com.book.store.data.mapper;

import com.book.store.data.dto.AuthorResponse;
import com.book.store.data.dto.CreateAuthorRequest;
import com.book.store.data.dto.UpdateAuthorRequest;
import com.book.store.data.entity.Author;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AuthorMapper {

    public AuthorResponse map(Author entity) {
        if (entity == null) {
            return null;
        }

        UUID id = entity.getId();
        String name = entity.getName();

        return new AuthorResponse(id, name);
    }

    public Author map(CreateAuthorRequest dto) {
        if (dto == null) {
            return null;
        }

        Author author = new Author();
        author.setName(dto.getName());
        author.setId(java.util.UUID.randomUUID());

        return author;
    }

    public void merge(Author entity, UpdateAuthorRequest dto) {
        if (dto == null) {
            return;
        }

        if (dto.getName() != null) {
            entity.setName(dto.getName());
        }
    }
}
