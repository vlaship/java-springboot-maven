package com.book.store.facade.controller;

import com.book.store.facade.model.AuthorFacadeResponse;
import com.book.store.facade.model.CreateAuthorFacadeRequest;
import com.book.store.facade.model.UpdateAuthorFacadeRequest;
import com.book.store.facade.service.AuthorService;
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
@RequestMapping("/v1/author")
public class AuthorController {

    private final AuthorService service;

    @GetMapping("/{id}")
    public ResponseEntity<AuthorFacadeResponse> getAuthorById(@NotNull @PathVariable("id") UUID id) {
        AuthorFacadeResponse body = service.findById(id);
        return ResponseEntity.ok(body);
    }

    @GetMapping
    public ResponseEntity<List<AuthorFacadeResponse>> getAuthors() {
        List<AuthorFacadeResponse> body = service.findAll();
        return ResponseEntity.ok(body);
    }

    @PostMapping
    public ResponseEntity<AuthorFacadeResponse> create(@NotNull @RequestBody CreateAuthorFacadeRequest request) {
        AuthorFacadeResponse bookResponse = service.create(request);
        return ResponseEntity.ok(bookResponse);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AuthorFacadeResponse> update(
            @NotNull @PathVariable("id") UUID id,
            @NotNull @RequestBody UpdateAuthorFacadeRequest request
    ) {
        AuthorFacadeResponse bookResponse = service.update(id, request);
        return ResponseEntity.ok(bookResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@NotNull @PathVariable("id") UUID id) {
        service.delete(id);
        return ResponseEntity.accepted().build();
    }
}
