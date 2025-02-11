package com.book.store.data.mapper;

import com.book.store.data.dto.CreateStoreRequest;
import com.book.store.data.dto.StoreResponse;
import com.book.store.data.dto.UpdateStoreRequest;
import com.book.store.data.entity.Store;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class StoreMapper {

    public StoreResponse map(Store entity) {
        if (entity == null) {
            return null;
        }

        UUID id = entity.getId();
        String name = entity.getName();
        String address = entity.getAddress();

        return new StoreResponse(id, name, address);
    }

    public Store map(CreateStoreRequest dto) {
        if (dto == null) {
            return null;
        }

        Store store = new Store();
        store.setName(dto.getName());
        store.setAddress(dto.getAddress());
        store.setId(java.util.UUID.randomUUID());

        return store;
    }

    public void merge(Store entity, UpdateStoreRequest dto) {
        if (dto == null) {
            return;
        }

        if (dto.getName() != null) {
            entity.setName(dto.getName());
        }
        if (dto.getAddress() != null) {
            entity.setAddress(dto.getAddress());
        }
    }
}
