package com.ncu.banking.exception;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(String accountId) {
        super("Account does not exist for this account ID: " + accountId);
    }
}
