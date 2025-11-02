package com.ncu.banking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class AccountDto {

    @JsonProperty("accountId")
    private String accountId;

    @JsonProperty("accountHolderName")
    private String accountHolderName;

    @JsonProperty("balance")
    private double balance;

    @JsonProperty("accountType")
    private String accountType;

    // ✅ Add transactions list
    @JsonProperty("transactions")
    private List<TransactionDto> transactions;

    public AccountDto() {}

    public AccountDto(String accountId, String accountHolderName, double balance, String accountType) {
        this.accountId = accountId;
        this.accountHolderName = accountHolderName;
        this.balance = balance;
        this.accountType = accountType;
    }

    // --- Getters and Setters ---
    public String getAccountId() { return accountId; }
    public void setAccountId(String accountId) { this.accountId = accountId; }

    public String getAccountHolderName() { return accountHolderName; }
    public void setAccountHolderName(String accountHolderName) { this.accountHolderName = accountHolderName; }

    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }

    public String getAccountType() { return accountType; }
    public void setAccountType(String accountType) { this.accountType = accountType; }

    public List<TransactionDto> getTransactions() { return transactions; }
    public void setTransactions(List<TransactionDto> transactions) { this.transactions = transactions; }
}
