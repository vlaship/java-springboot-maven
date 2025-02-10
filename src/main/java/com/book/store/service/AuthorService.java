package com.book.store.service;

import com.book.store.dto.AuthorResponse;
import com.book.store.dto.CreateAuthorRequest;
import com.book.store.dto.UpdateAuthorRequest;
import com.book.store.exception.NotFoundException;
import com.book.store.mapper.AuthorMapper;
import com.book.store.repository.AuthorRepository;

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
public class AuthorService {

    private final AuthorRepository repository;
    private final AuthorMapper mapper;

    public AuthorResponse create(CreateAuthorRequest dto) {
        log.debug("create({})", dto);
        var author = mapper.map(dto);
        var saved = repository.save(author);
        return mapper.map(saved);
    }

    public AuthorResponse update(UUID id, UpdateAuthorRequest dto) {
        log.debug("update({},{})", id, dto);
        var author = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("author", id));
        mapper.merge(author, dto);
        var saved = repository.save(author);
        return mapper.map(saved);
    }

    public void delete(UUID id) {
        log.debug("delete({})", id);
        repository.deleteById(id);
    }

    public List<AuthorResponse> findAll() {
        log.debug("findAll()");
        return StreamSupport
                .stream(repository.findAll().spliterator(), false)
                .map(mapper::map)
                .toList();
    }

    public AuthorResponse findById(UUID id) {
        log.debug("findById({})", id);
        var author = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("author", id));
        return mapper.map(author);
    }
}
