package io.github.vlaship.book.catalog.service;

import io.github.vlaship.book.catalog.dto.StoreResponse;
import io.github.vlaship.book.catalog.dto.CreateStoreRequest;
import io.github.vlaship.book.catalog.dto.UpdateStoreRequest;
import io.github.vlaship.book.catalog.exception.NotFoundException;
import io.github.vlaship.book.catalog.mapper.StoreMapper;
import io.github.vlaship.book.catalog.repository.StoreRepository;
import java.util.List;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class StoreService {

  private final StoreRepository repository;
  private final StoreMapper mapper;

  @Transactional
  public StoreResponse create(CreateStoreRequest dto) {
    log.debug("create({})", dto);
    var author = mapper.map(dto);
    var saved = repository.save(author);
    return mapper.map(saved);
  }

  @Transactional
  public StoreResponse update(Long id, UpdateStoreRequest dto) {
    log.debug("update({},{})", id, dto);
    var author = repository.findById(id)
        .orElseThrow(() -> new NotFoundException("store", id));
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
  public List<StoreResponse> findAll() {
    log.debug("findAll()");
    return StreamSupport
        .stream(repository.findAll().spliterator(), false)
        .map(mapper::map)
        .toList();
  }

  @Transactional(readOnly = true)
  public StoreResponse findById(Long id) {
    log.debug("findById({})", id);
    var author = repository.findById(id)
        .orElseThrow(() -> new NotFoundException("store", id));
    return mapper.map(author);
  }
}