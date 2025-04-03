package com.book.store.facade.controller;

import com.book.store.facade.model.UserFacadeResponse;
import com.book.store.facade.model.CreateUserFacadeRequest;
import com.book.store.facade.model.UpdateUserFacadeRequest;
import com.book.store.facade.service.UserService;

import javax.validation.constraints.NotNull;

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
@RequestMapping("/v1/user")
public class UserController {

    private final UserService service;

    @GetMapping("/{id}")
    public ResponseEntity<UserFacadeResponse> getUserById(@NotNull @PathVariable("id") UUID id) {
        UserFacadeResponse body = service.findById(id);
        return ResponseEntity.ok(body);
    }

    @PostMapping
    public ResponseEntity<UserFacadeResponse> create(@NotNull @RequestBody CreateUserFacadeRequest request) {
        UserFacadeResponse userResponse = service.create(request);
        return ResponseEntity.ok(userResponse);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserFacadeResponse> update(
            @NotNull @PathVariable("id") UUID id,
            @NotNull @RequestBody UpdateUserFacadeRequest request
    ) {
        UserFacadeResponse userResponse = service.update(id, request);
        return ResponseEntity.ok(userResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@NotNull @PathVariable("id") UUID id) {
        service.delete(id);
        return ResponseEntity.accepted().build();
    }
}
