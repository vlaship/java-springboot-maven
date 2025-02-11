package com.book.store.data.service.impl;

import com.book.store.data.dto.AuthorResponse;
import com.book.store.data.dto.CreateAuthorRequest;
import com.book.store.data.dto.UpdateAuthorRequest;
import com.book.store.data.entity.Author;
import com.book.store.data.exception.NotFoundException;
import com.book.store.data.mapper.AuthorMapper;
import com.book.store.data.repository.AuthorRepository;
import com.book.store.data.service.AuthorService;
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
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository repository;
    private final AuthorMapper mapper;

    @Override
    public AuthorResponse create(CreateAuthorRequest dto) {
        log.debug("create({})", dto);
        Author author = mapper.map(dto);
        Author saved = repository.save(author);
        return mapper.map(saved);
    }

    @Override
    public AuthorResponse update(UUID id, UpdateAuthorRequest dto) {
        log.debug("update({},{})", id, dto);
        Author author = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("author", id));
        mapper.merge(author, dto);
        Author saved = repository.save(author);
        return mapper.map(saved);
    }

    @Override
    public void delete(UUID id) {
        log.debug("delete({})", id);
        repository.deleteById(id);
    }

    @Override
    public List<AuthorResponse> findAll() {
        log.debug("findAll()");
        return repository.findAll()
                .stream()
                .map(mapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public AuthorResponse findById(UUID id) {
        log.debug("findById({})", id);
        Author author = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("author", id));
        return mapper.map(author);
    }
}
