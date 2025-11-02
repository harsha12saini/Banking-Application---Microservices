package com.ncu.banking.model;

public class Account {
    private String accountId;
    private String customerId; // ✅ Added customerId to link account to customer
    private String accountHolderName;
    private String email;
    private String password;
    private double balance;
    private String accountType;

    // Default constructor
    public Account() {}

    // Full constructor
    public Account(String accountId, String customerId, String accountHolderName, String email, String password, double balance, String accountType) {
        this.accountId = accountId;
        this.customerId = customerId; // ✅ initialize customerId
        this.accountHolderName = accountHolderName;
        this.email = email;
        this.password = password;
        this.balance = balance;
        this.accountType = accountType;
    }

    // Getters and Setters
    public String getAccountId() { return accountId; }
    public void setAccountId(String accountId) { this.accountId = accountId; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getAccountHolderName() { return accountHolderName; }
    public void setAccountHolderName(String accountHolderName) { this.accountHolderName = accountHolderName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }

    public String getAccountType() { return accountType; }
    public void setAccountType(String accountType) { this.accountType = accountType; }

    @Override
    public String toString() {
        return "Account{" +
                "accountId='" + accountId + '\'' +
                ", customerId='" + customerId + '\'' +
                ", accountHolderName='" + accountHolderName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", balance=" + balance +
                ", accountType='" + accountType + '\'' +
                '}';
    }
}
