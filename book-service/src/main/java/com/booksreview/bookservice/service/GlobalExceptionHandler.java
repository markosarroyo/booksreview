package com.booksreview.bookservice.service;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Validation errors in @RequestBody (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,Object>> handelValidationErrors(MethodArgumentNotValidException ex){
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();

        Map <String,Object> body = Map.of("errors",errors);

        return ResponseEntity.badRequest().body(body);
    }

    // Validation errors for @PathVariable or @RequestParam (@Validated)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String,Object>> handleConstraintValidation(ConstraintViolationException ex){
        List<String> errors = ex.getConstraintViolations()
                .stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .toList();

        Map<String, Object> body = Map.of(
                "errors", errors
        );

        return ResponseEntity.badRequest().body(body);


    }

    //Runtime Exception
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String,Object>> handleRuntimeException(RuntimeException ex){
        List<String> errors = List.of(ex.getMessage());
        Map<String,Object> body = Map.of("errors:",errors);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);

    }

    // Unexpected error
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneral(Exception ex) {

        Map<String, String> body = Map.of(
                "error", "Error interno del servidor"
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

}
