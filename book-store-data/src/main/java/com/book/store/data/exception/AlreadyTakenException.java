package com.book.store.data.exception;

import lombok.Getter;

@Getter
public class AlreadyTakenException extends RuntimeException {
    public AlreadyTakenException(String msg) {
        super(msg);
    }
}
