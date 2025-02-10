package com.book.store.data.service;

import com.book.store.data.dto.StoreResponse;
import com.book.store.data.dto.CreateStoreRequest;
import com.book.store.data.dto.UpdateStoreRequest;
import com.book.store.data.exception.NotFoundException;
import com.book.store.data.mapper.StoreMapper;
import com.book.store.data.repository.StoreRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository repository;
    private final StoreMapper mapper;

    public StoreResponse create(CreateStoreRequest dto) {
        log.debug("create({})", dto);
        var author = mapper.map(dto);
        var saved = repository.save(author);
        return mapper.map(saved);
    }

    public StoreResponse update(UUID id, UpdateStoreRequest dto) {
        log.debug("update({},{})", id, dto);
        var author = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("store", id));
        mapper.merge(author, dto);
        var saved = repository.save(author);
        return mapper.map(saved);
    }

    public void delete(UUID id) {
        log.debug("delete({})", id);
        repository.deleteById(id);
    }

    public List<StoreResponse> findAll() {
        log.debug("findAll()");
        return StreamSupport
                .stream(repository.findAll().spliterator(), false)
                .map(mapper::map)
                .toList();
    }

    public StoreResponse findById(UUID id) {
        log.debug("findById({})", id);
        var author = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("store", id));
        return mapper.map(author);
    }
}