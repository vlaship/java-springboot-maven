package com.book.store.data.service.impl;

import com.book.store.data.dto.CreateStoreRequest;
import com.book.store.data.dto.StoreResponse;
import com.book.store.data.dto.UpdateStoreRequest;
import com.book.store.data.entity.Store;
import com.book.store.data.exception.NotFoundException;
import com.book.store.data.mapper.StoreMapper;
import com.book.store.data.repository.StoreRepository;
import com.book.store.data.service.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final StoreRepository repository;
    private final StoreMapper mapper;

    @Override
    public StoreResponse create(CreateStoreRequest dto) {
        log.debug("create({})", dto);
        Store store = mapper.map(dto);
        Store saved = repository.save(store);
        return mapper.map(saved);
    }

    @Override
    public StoreResponse update(UUID id, UpdateStoreRequest dto) {
        log.debug("update({},{})", id, dto);
        Store store = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("store", id));
        mapper.merge(store, dto);
        Store saved = repository.save(store);
        return mapper.map(saved);
    }

    @Override
    public void delete(UUID id) {
        log.debug("delete({})", id);
        repository.deleteById(id);
    }

    @Override
    public List<StoreResponse> findAll() {
        log.debug("findAll()");
        return repository.findAll()
                .stream()
                .map(mapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public StoreResponse findById(UUID id) {
        log.debug("findById({})", id);
        Store store = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("store", id));
        return mapper.map(store);
    }
}