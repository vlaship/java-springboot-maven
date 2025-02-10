package com.book.store.facade.controller;

import com.book.store.facade.model.BookFacadeResponse;
import com.book.store.facade.model.CreateBookFacadeRequest;
import com.book.store.facade.model.UpdateBookFacadeRequest;
import com.book.store.facade.service.BookService;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

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
    public ResponseEntity<BookFacadeResponse> findBookById(@NotNull @PathVariable("id") UUID id) {
        var body = service.findById(id);
        return ResponseEntity.ok(body);
    }

    @GetMapping("/author/{authorId}")
    public ResponseEntity<List<BookFacadeResponse>> findBooksByAuthorId(
            @NotNull @PathVariable("authorId") UUID authorId
    ) {
        var body = service.findBooksByAuthorId(authorId);
        return ResponseEntity.ok(body);
    }

    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<BookFacadeResponse>> findBooksByStoreId(
            @NotNull @PathVariable("storeId") UUID storeId
    ) {
        var body = service.findBooksByStoreId(storeId);
        return ResponseEntity.ok(body);
    }

    @PostMapping
    public ResponseEntity<BookFacadeResponse> create(@NotNull @RequestBody CreateBookFacadeRequest request) {
        var bookResponse = service.create(request);
        return ResponseEntity.ok(bookResponse);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BookFacadeResponse> update(
            @NotNull @PathVariable("id") UUID id,
            @NotNull @RequestBody UpdateBookFacadeRequest request
    ) {
        var bookResponse = service.update(id, request);
        return ResponseEntity.ok(bookResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@NotNull @PathVariable("id") UUID id) {
        service.delete(id);
        return ResponseEntity.accepted().build();
    }
}
