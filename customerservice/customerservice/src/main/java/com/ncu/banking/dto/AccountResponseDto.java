package com.ncu.banking.dto;

import java.util.List;

public class AccountResponseDto {

    private String status;          
    private String message;         
    private List<AccountDto> accounts;  

    public AccountResponseDto() {}

    public AccountResponseDto(String status, String message, List<AccountDto> accounts) {
        this.status = status;
        this.message = message;
        this.accounts = accounts;
    }

    // --- Getters & Setters ---
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public List<AccountDto> getAccounts() { return accounts; }
    public void setAccounts(List<AccountDto> accounts) { this.accounts = accounts; }
}
