package io.github.vlaship.book.catalog.service;

import io.github.vlaship.book.catalog.dto.AuthorResponse;
import io.github.vlaship.book.catalog.dto.CreateAuthorRequest;
import io.github.vlaship.book.catalog.dto.UpdateAuthorRequest;
import io.github.vlaship.book.catalog.exception.NotFoundException;
import io.github.vlaship.book.catalog.mapper.AuthorMapper;
import io.github.vlaship.book.catalog.repository.AuthorRepository;
import java.util.List;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthorService {

  private final AuthorRepository repository;
  private final AuthorMapper mapper;

  @Transactional
  public AuthorResponse create(CreateAuthorRequest dto) {
    log.debug("create({})", dto);
    var author = mapper.map(dto);
    var saved = repository.save(author);
    return mapper.map(saved);
  }

  @Transactional
  public AuthorResponse update(Long id, UpdateAuthorRequest dto) {
    log.debug("update({},{})", id, dto);
    var author = repository.findById(id)
        .orElseThrow(() -> new NotFoundException("author", id));
    mapper.merge(author, dto);
    var saved = repository.save(author);
    return mapper.map(saved);
  }

  @Transactional
  public void delete(Long id) {
    log.debug("delete({})", id);
    repository.deleteById(id);
  }

  @Transactional(readOnly = true)
  public List<AuthorResponse> findAll() {
    log.debug("findAll()");
    return StreamSupport
        .stream(repository.findAll().spliterator(), false)
        .map(mapper::map)
        .toList();
  }

  @Transactional(readOnly = true)
  public AuthorResponse findById(Long id) {
    log.debug("findById({})", id);
    var author = repository.findById(id)
        .orElseThrow(() -> new NotFoundException("author", id));
    return mapper.map(author);
  }

}
