package com.ncu.banking.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ncu.banking.dto.AuthResponseDto;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<AuthResponseDto> handleUserAlreadyExists(UserAlreadyExistsException ex) {
        AuthResponseDto response = new AuthResponseDto();
        response.setStatus("Signup failed: " + ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<AuthResponseDto> handleUserNotFound(UserNotFoundException ex) {
        AuthResponseDto response = new AuthResponseDto();
        response.setStatus("Authentication failed: " + ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DatabaseConnectionException.class)
    public ResponseEntity<AuthResponseDto> handleDatabase(DatabaseConnectionException ex) {
        AuthResponseDto response = new AuthResponseDto();
        response.setStatus("Database error: " + ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<AuthResponseDto> handleGeneral(Exception ex) {
        AuthResponseDto response = new AuthResponseDto();
        response.setStatus("Unexpected error: " + ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
