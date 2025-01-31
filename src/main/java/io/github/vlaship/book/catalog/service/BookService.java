package io.github.vlaship.book.catalog.service;

import io.github.vlaship.book.catalog.dto.BookListResponse;
import io.github.vlaship.book.catalog.dto.CreateBookRequest;
import io.github.vlaship.book.catalog.dto.BookResponse;
import io.github.vlaship.book.catalog.dto.UpdateBookRequest;
import io.github.vlaship.book.catalog.entity.BookStore;
import io.github.vlaship.book.catalog.exception.NotFoundException;
import io.github.vlaship.book.catalog.mapper.BookMapper;
import io.github.vlaship.book.catalog.repository.BookRepository;
import io.github.vlaship.book.catalog.repository.BookStoreRepository;
import java.util.List;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookService {

  private final BookRepository repository;
  private final BookMapper mapper;
  private final BookStoreRepository bookStoreRepository;

  @Transactional
  public BookResponse create(CreateBookRequest dto) {
    log.debug("create({})", dto);
    var author = mapper.map(dto);
    var saved = repository.save(author);
    var storeIds = getStoreIds(saved.id());
    return mapper.map(saved, storeIds);
  }

  @Transactional
  public BookResponse update(Long id, UpdateBookRequest dto) {
    log.debug("update({},{})", id, dto);
    var author = repository.findById(id)
        .orElseThrow(() -> new NotFoundException("book", id));
    mapper.merge(dto, author);
    var saved = repository.save(author);
    var storeIds = getStoreIds(saved.id());
    return mapper.map(saved, storeIds);
  }

  @Transactional
  public void delete(Long id) {
    log.debug("delete({})", id);
    repository.deleteById(id);
  }

  @Transactional(readOnly = true)
  public BookResponse findById(Long id) {
    log.debug("findById({})", id);
    var author = repository.findById(id)
        .orElseThrow(() -> new NotFoundException("book", id));
    var storeIds = getStoreIds(id);
    return mapper.map(author, storeIds);
  }

  private List<Long> getStoreIds(Long bookId) {
    log.debug("getStoreIds({})", bookId);
    return bookStoreRepository.findByBookId(bookId)
        .stream()
        .map(BookStore::storeId)
        .toList();
  }
}
