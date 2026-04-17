package ru.cdek.test.task.cdek_test_task.exception.exceptions;

import java.time.LocalDateTime;
import java.util.List;

public record ValidationResponseException(
        List<String> errors,
        LocalDateTime timestamp
) {
    public ValidationResponseException(List<String> errors) {
        this(errors, LocalDateTime.now());
    }
}