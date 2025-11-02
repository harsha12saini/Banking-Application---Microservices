package com.ncu.banking.dto;

public class CustomerResponseDto {
    private String customerId;
    private String status;
    private String message;

    public CustomerResponseDto() {}

    public CustomerResponseDto(String customerId, String status, String message) {
        this.customerId = customerId;
        this.status = status;
        this.message = message;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
