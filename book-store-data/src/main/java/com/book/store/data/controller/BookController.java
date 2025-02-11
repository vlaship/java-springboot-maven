package com.book.store.data.controller;

import com.book.store.data.dto.BookResponse;
import com.book.store.data.dto.CreateBookRequest;
import com.book.store.data.dto.UpdateBookRequest;
import com.book.store.data.service.BookService;

import javax.validation.constraints.NotNull;

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
    public ResponseEntity<BookResponse> findBookById(@NotNull @PathVariable("id") UUID id) {
        BookResponse body = service.findById(id);
        return ResponseEntity.ok(body);
    }

    @GetMapping("/author/{authorId}")
    public ResponseEntity<List<BookResponse>> findBooksByAuthorId(
            @NotNull @PathVariable("authorId") UUID authorId
    ) {
        List<BookResponse> body = service.findBooksByAuthorId(authorId);
        return ResponseEntity.ok(body);
    }

    @PostMapping
    public ResponseEntity<BookResponse> create(@NotNull @RequestBody CreateBookRequest request) {
        BookResponse bookResponse = service.create(request);
        return ResponseEntity.ok(bookResponse);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BookResponse> update(
            @NotNull @PathVariable("id") UUID id,
            @NotNull @RequestBody UpdateBookRequest request
    ) {
        BookResponse bookResponse = service.update(id, request);
        return ResponseEntity.ok(bookResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@NotNull @PathVariable("id") UUID id) {
        service.delete(id);
        return ResponseEntity.accepted().build();
    }
}
