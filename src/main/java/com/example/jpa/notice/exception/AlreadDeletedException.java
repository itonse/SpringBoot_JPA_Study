package com.example.jpa.notice.exception;

public class AlreadDeletedException extends RuntimeException {
    public AlreadDeletedException(String message) {
        super(message);
    }
}
