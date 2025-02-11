package com.book.store.data.service;

import com.book.store.data.dto.StoreResponse;
import com.book.store.data.dto.CreateStoreRequest;
import com.book.store.data.dto.UpdateStoreRequest;

import java.util.List;
import java.util.UUID;

public interface StoreService {

    StoreResponse create(CreateStoreRequest dto);

    StoreResponse update(UUID id, UpdateStoreRequest dto);

    void delete(UUID id);

    List<StoreResponse> findAll();

    StoreResponse findById(UUID id);
}