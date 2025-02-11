package com.book.store.facade.controller;

import com.book.store.facade.model.StoreFacadeResponse;
import com.book.store.facade.model.CreateStoreFacadeRequest;
import com.book.store.facade.model.UpdateStoreFacadeRequest;
import com.book.store.facade.service.StoreService;

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
@RequestMapping("/v1/store")
public class StoreController {

    private final StoreService service;

    @GetMapping("/{id}")
    public ResponseEntity<StoreFacadeResponse> getStoreById(@NotNull @PathVariable("id") UUID id) {
        StoreFacadeResponse body = service.findById(id);
        return ResponseEntity.ok(body);
    }

    @GetMapping
    public ResponseEntity<List<StoreFacadeResponse>> getStores() {
        List<StoreFacadeResponse> body = service.findAll();
        return ResponseEntity.ok(body);
    }

    @PostMapping
    public ResponseEntity<StoreFacadeResponse> create(@NotNull @RequestBody CreateStoreFacadeRequest request) {
        StoreFacadeResponse bookResponse = service.create(request);
        return ResponseEntity.ok(bookResponse);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<StoreFacadeResponse> update(
            @NotNull @PathVariable("id") UUID id,
            @NotNull @RequestBody UpdateStoreFacadeRequest request
    ) {
        StoreFacadeResponse bookResponse = service.update(id, request);
        return ResponseEntity.ok(bookResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@NotNull @PathVariable("id") UUID id) {
        service.delete(id);
        return ResponseEntity.accepted().build();
    }
}
