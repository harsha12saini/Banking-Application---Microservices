package com.ncu.banking.dto;

public class AuthResponseDto {
    private String email;
    private String status;
    private String message;

    // Constructors
    public AuthResponseDto() {}
    public AuthResponseDto(String email, String status, String message) {
        this.email = email;
        this.status = status;
        this.message = message;
    }

    // Getters & Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
