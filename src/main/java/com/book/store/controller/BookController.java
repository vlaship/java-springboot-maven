package com.book.store.controller;

import com.book.store.dto.BookResponse;
import com.book.store.dto.CreateBookRequest;
import com.book.store.dto.UpdateBookRequest;
import com.book.store.service.BookService;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/book")
public class BookController {

  private final BookService service;

  @GetMapping("/{id}")
  public ResponseEntity<BookResponse> findBookById(@NotNull @PathVariable("id") Long id) {
    var body = service.findById(id);
    return ResponseEntity.ok(body);
  }

  @GetMapping("/author/{authorId}")
  public ResponseEntity<List<BookResponse>> findBooksByAuthorId(
      @NotNull @PathVariable("authorId") Long authorId
  ) {
    var body = service.findBooksByAuthorId(authorId);
    return ResponseEntity.ok(body);
  }

  @GetMapping("/store/{storeId}")
  public ResponseEntity<List<BookResponse>> findBooksByStoreId(
      @NotNull @PathVariable("storeId") Long storeId
  ) {
    var body = service.findBooksByStoreId(storeId);
    return ResponseEntity.ok(body);
  }

  @PostMapping
  public ResponseEntity<BookResponse> create(@NotNull @RequestBody CreateBookRequest request) {
    var bookResponse = service.create(request);
    return ResponseEntity.ok(bookResponse);
  }

  @PatchMapping("/{id}")
  public ResponseEntity<BookResponse> update(
      @NotNull @PathVariable("id") Long id,
      @NotNull @RequestBody UpdateBookRequest request
  ) {
    var bookResponse = service.update(id, request);
    return ResponseEntity.ok(bookResponse);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@NotNull @PathVariable("id") Long id) {
    service.delete(id);
    return ResponseEntity.accepted().build();
  }
}
