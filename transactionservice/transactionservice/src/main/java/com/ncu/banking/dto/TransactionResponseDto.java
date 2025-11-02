package com.ncu.banking.dto;

import java.util.List;

public class TransactionResponseDto {
    private String status;                // Success / Failed
    private String message;               // Detailed error or info
    private List<TransactionDto> data;    // List of transactions (null in error)
    
    public TransactionResponseDto() {}

    public TransactionResponseDto(String status, String message, List<TransactionDto> data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    // Getters & Setters
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public List<TransactionDto> getData() { return data; }
    public void setData(List<TransactionDto> data) { this.data = data; }
}
