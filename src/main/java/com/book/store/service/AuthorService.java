package com.book.store.service;

import com.book.store.dto.AuthorResponse;
import com.book.store.dto.CreateAuthorRequest;
import com.book.store.dto.UpdateAuthorRequest;
import com.book.store.exception.NotFoundException;
import com.book.store.mapper.AuthorMapper;
import com.book.store.repository.AuthorRepository;
import java.util.List;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthorService {

  private final AuthorRepository repository;
  private final AuthorMapper mapper;

  @NonNull
  @Transactional
  public AuthorResponse create(@NonNull CreateAuthorRequest dto) {
    log.debug("create({})", dto);
    var author = mapper.map(dto);
    var saved = repository.save(author);
    return mapper.map(saved);
  }

  @NonNull
  @Transactional
  public AuthorResponse update(@NonNull Long id, @NonNull UpdateAuthorRequest dto) {
    log.debug("update({},{})", id, dto);
    var author = repository.findById(id)
        .orElseThrow(() -> new NotFoundException("author", id));
    mapper.merge(author, dto);
    var saved = repository.save(author);
    return mapper.map(saved);
  }

  @Transactional
  public void delete(@NonNull Long id) {
    log.debug("delete({})", id);
    repository.deleteById(id);
  }

  @NonNull
  @Transactional(readOnly = true)
  public List<AuthorResponse> findAll() {
    log.debug("findAll()");
    return StreamSupport
        .stream(repository.findAll().spliterator(), false)
        .map(mapper::map)
        .toList();
  }

  @NonNull
  @Transactional(readOnly = true)
  public AuthorResponse findById(@NonNull Long id) {
    log.debug("findById({})", id);
    var author = repository.findById(id)
        .orElseThrow(() -> new NotFoundException("author", id));
    return mapper.map(author);
  }

}
