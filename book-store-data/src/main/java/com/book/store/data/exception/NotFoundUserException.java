package com.book.store.data.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NotFoundUserException extends RuntimeException {
    private final String username;
}
