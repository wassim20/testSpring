package com.elokencia.springApp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 404 - Resource Not Found
    @ExceptionHandler({RuntimeException.class, java.util.NoSuchElementException.class})
    public ResponseEntity<Map<String, Object>> handleNotFound(Exception e) {
        return buildResponse("Not Found", e.getMessage(), HttpStatus.NOT_FOUND);
    }

    // 400 - Bad Request (e.g., invalid input, ID mismatch)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleBadRequest(Exception e) {
        return buildResponse("Bad Request", e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // 500 - Any unhandled exception
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleInternal(Exception e) {
        return buildResponse("Internal Server Error", e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Helper: Build consistent error response
    private ResponseEntity<Map<String, Object>> buildResponse(
            String error, String message, HttpStatus status) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", error);
        body.put("message", message != null ? message : "An unknown error occurred");
        body.put("status", status.value());
        body.put("timestamp", LocalDateTime.now().toString());

        return new ResponseEntity<>(body, status);
    }
}