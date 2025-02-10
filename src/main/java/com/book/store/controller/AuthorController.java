package com.book.store.controller;

import com.book.store.dto.AuthorResponse;
import com.book.store.dto.CreateAuthorRequest;
import com.book.store.dto.UpdateAuthorRequest;
import com.book.store.service.AuthorService;
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
@RequestMapping("/v1/author")
public class AuthorController {

    private final AuthorService service;

    @GetMapping("/{id}")
    public ResponseEntity<AuthorResponse> getAuthorById(@NotNull @PathVariable("id") UUID id) {
        var body = service.findById(id);
        return ResponseEntity.ok(body);
    }

    @GetMapping
    public ResponseEntity<List<AuthorResponse>> getAuthors() {
        var body = service.findAll();
        return ResponseEntity.ok(body);
    }

    @PostMapping
    public ResponseEntity<AuthorResponse> create(@NotNull @RequestBody CreateAuthorRequest request) {
        var bookResponse = service.create(request);
        return ResponseEntity.ok(bookResponse);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AuthorResponse> update(
            @NotNull @PathVariable("id") UUID id,
            @NotNull @RequestBody UpdateAuthorRequest request
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
