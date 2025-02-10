package com.book.store.controller;

import com.book.store.dto.StoreResponse;
import com.book.store.dto.CreateStoreRequest;
import com.book.store.dto.UpdateStoreRequest;
import com.book.store.service.StoreService;
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
@RequestMapping("/v1/store")
public class StoreController {

    private final StoreService service;

    @GetMapping("/{id}")
    public ResponseEntity<StoreResponse> getStoreById(@NotNull @PathVariable("id") UUID id) {
        var body = service.findById(id);
        return ResponseEntity.ok(body);
    }

    @GetMapping
    public ResponseEntity<List<StoreResponse>> getStores() {
        var body = service.findAll();
        return ResponseEntity.ok(body);
    }

    @PostMapping
    public ResponseEntity<StoreResponse> create(@NotNull @RequestBody CreateStoreRequest request) {
        var bookResponse = service.create(request);
        return ResponseEntity.ok(bookResponse);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<StoreResponse> update(
            @NotNull @PathVariable("id") UUID id,
            @NotNull @RequestBody UpdateStoreRequest request
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
