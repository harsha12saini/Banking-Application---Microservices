package com.ncu.banking.controller;

import com.ncu.banking.dto.TransactionDto;
import com.ncu.banking.dto.TransactionResponseDto;
import com.ncu.banking.model.Transaction;
import com.ncu.banking.service.TransactionService;
import com.ncu.banking.exception.TransactionNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/")
    public ResponseEntity<TransactionResponseDto> getAllTransactions() {
        List<TransactionDto> transactions = transactionService.getAllTransactions();
        TransactionResponseDto response = new TransactionResponseDto("Success", "Transactions fetched successfully", transactions);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionResponseDto> getTransactionById(@PathVariable String transactionId) {
        TransactionDto transaction = transactionService.getTransactionById(transactionId);
        if (transaction == null) {
            throw new TransactionNotFoundException("Transaction not found: " + transactionId);
        }
        TransactionResponseDto response = new TransactionResponseDto("Success", "Transaction fetched successfully", List.of(transaction));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<TransactionResponseDto> getTransactionsByAccount(@PathVariable String accountId) {
        List<TransactionDto> transactions = transactionService.getTransactionsByAccountId(accountId);
        if (transactions.isEmpty()) {
            throw new TransactionNotFoundException("No transactions found for account: " + accountId);
        }
        TransactionResponseDto response = new TransactionResponseDto("Success", "Transactions fetched successfully", transactions);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/")
    public ResponseEntity<TransactionResponseDto> createTransaction(@RequestBody Transaction transaction) {
        TransactionDto created = transactionService.addTransaction(transaction);
        TransactionResponseDto response = new TransactionResponseDto("Success", "Transaction created successfully", List.of(created));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{transactionId}")
    public ResponseEntity<TransactionResponseDto> deleteTransaction(@PathVariable String transactionId) {
        boolean deleted = transactionService.deleteTransaction(transactionId);
        if (!deleted) {
            throw new TransactionNotFoundException("Transaction not found: " + transactionId);
        }
        TransactionResponseDto response = new TransactionResponseDto("Success", "Transaction deleted successfully", null);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/page")
    public ResponseEntity<TransactionResponseDto> getTransactionsNextPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        List<TransactionDto> transactions = transactionService.getTransactionsByPage(page, size).getTransactions();
        TransactionResponseDto response = new TransactionResponseDto("Success", "Transactions fetched successfully", transactions);
        return ResponseEntity.ok(response);
    }
}
