package com.book.store.data.repository;

import com.book.store.data.entity.Store;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface StoreRepository extends CrudRepository<Store, UUID> {

}
