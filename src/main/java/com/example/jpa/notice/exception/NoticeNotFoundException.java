package com.example.jpa.notice.exception;

public class NoticeNotFoundException extends RuntimeException {
    public NoticeNotFoundException(String message) {
        super(message);  // 메세지를 던짐
    }
}
