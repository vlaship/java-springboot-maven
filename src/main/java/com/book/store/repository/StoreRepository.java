package com.book.store.repository;

import com.book.store.entity.Store;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface StoreRepository extends CrudRepository<Store, UUID> {

}
