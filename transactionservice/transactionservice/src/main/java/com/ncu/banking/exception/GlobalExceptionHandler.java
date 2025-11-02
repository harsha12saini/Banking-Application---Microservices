package com.ncu.banking.exception;

import com.ncu.banking.dto.TransactionResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<TransactionResponseDto> handleAccountNotFound(AccountNotFoundException ex) {
        return buildErrorResponse("Failed", ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TransactionNotFoundException.class)
    public ResponseEntity<TransactionResponseDto> handleTransactionNotFound(TransactionNotFoundException ex) {
        return buildErrorResponse("Failed", ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<TransactionResponseDto> handleDatabaseException(DatabaseException ex) {
        return buildErrorResponse("Failed", "Database error: " + ex.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<TransactionResponseDto> handleGenericException(Exception ex) {
        return buildErrorResponse("Failed", "Unexpected error: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<TransactionResponseDto> buildErrorResponse(String status, String message, HttpStatus code) {
        TransactionResponseDto response = new TransactionResponseDto();
        response.setStatus(status);
        response.setMessage(message);
        response.setData(null);
        return ResponseEntity.status(code).body(response);
    }
}
