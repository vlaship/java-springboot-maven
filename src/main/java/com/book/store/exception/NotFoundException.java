package com.book.store.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class NotFoundException extends RuntimeException {
    private final String type;
    private final UUID id;
}
