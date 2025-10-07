package com.omni.main.ValidateUserNames.exceptionHandler;

import com.omni.main.ValidateUserNames.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        // Get first validation error message WITHOUT field name
        String errorMsg = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(error -> error.getDefaultMessage()) // <-- remove field name
                .findFirst()
                .orElse("Validation error");

        return ResponseEntity.ok(ApiResponse.error(errorMsg));
    }

    @ExceptionHandler(UsernameAlreadyTakenException.class)
    public ResponseEntity<ApiResponse<?>> handleUsernameTaken(UsernameAlreadyTakenException ex) {
        return ResponseEntity.ok(new ApiResponse<>(
                new ApiResponse.Status("000404", ex.getMessage()), List.of()
        ));
    }

    @ExceptionHandler(UsernameHistoryViolationException.class)
    public ResponseEntity<ApiResponse<?>> handleHistoryViolation(UsernameHistoryViolationException ex) {
        return ResponseEntity.ok(new ApiResponse<>(
                new ApiResponse.Status("000404", ex.getMessage()), List.of()
        ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleGeneric(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Internal server error"));
    }
}