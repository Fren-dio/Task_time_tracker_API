package ru.cdek.test.task.cdek_test_task.exception.exceptions;

import java.time.LocalDateTime;

public record ResponseException(
        String message,
        LocalDateTime timestamp
) {
    public ResponseException(String message) {
        this(message, LocalDateTime.now());
    }
}