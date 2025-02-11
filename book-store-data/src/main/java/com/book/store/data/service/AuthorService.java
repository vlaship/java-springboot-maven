package com.book.store.data.service;

import com.book.store.data.dto.AuthorResponse;
import com.book.store.data.dto.CreateAuthorRequest;
import com.book.store.data.dto.UpdateAuthorRequest;

import java.util.List;
import java.util.UUID;

public interface AuthorService {

    AuthorResponse create(CreateAuthorRequest dto);

    AuthorResponse update(UUID id, UpdateAuthorRequest dto);

    void delete(UUID id);

    List<AuthorResponse> findAll();

    AuthorResponse findById(UUID id);
}
