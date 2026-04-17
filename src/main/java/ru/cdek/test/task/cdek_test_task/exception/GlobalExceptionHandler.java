package ru.cdek.test.task.cdek_test_task.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.cdek.test.task.cdek_test_task.exception.exceptions.ResourceNotFoundException;
import ru.cdek.test.task.cdek_test_task.exception.exceptions.ResponseException;
import ru.cdek.test.task.cdek_test_task.exception.exceptions.ValidationResponseException;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ResponseException> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ResponseException("Внутренняя ошибка сервера: " + ex.getMessage()));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResponseException> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseException(ex.getMessage()));
    }

    // Обработка ошибок валидации @Valid (для @RequestBody)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationResponseException> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ValidationResponseException(errors));
    }

    // Обработка ошибок валидации для @RequestParam, @PathVariable (если используются)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ValidationResponseException> handleConstraintViolation(ConstraintViolationException ex) {
        List<String> errors = ex.getConstraintViolations().stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ValidationResponseException(errors));
    }

    // Общий обработчик IllegalArgumentException (например, при неверных аргументах)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseException> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseException(ex.getMessage()));
    }

}
