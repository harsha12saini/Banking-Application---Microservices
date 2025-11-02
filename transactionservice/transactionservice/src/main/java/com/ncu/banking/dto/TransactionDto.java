package com.ncu.banking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionDto {

    @JsonProperty("transactionId")
    private String transactionId;

    @JsonProperty("accountId")
    private String accountId;

    @JsonProperty("amount")
    private BigDecimal amount;

    @JsonProperty("type")
    private String type; 

    @JsonProperty("timestamp")
    private LocalDateTime timestamp;

    public TransactionDto() {}

    public TransactionDto(String transactionId, String accountId, BigDecimal amount, String type, LocalDateTime timestamp) {
        this.transactionId = transactionId;
        this.accountId = accountId;
        this.amount = amount;
        this.type = type;
        this.timestamp = timestamp;
    }


    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
    public String getAccountId() { return accountId; }
    public void setAccountId(String accountId) { this.accountId = accountId; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
