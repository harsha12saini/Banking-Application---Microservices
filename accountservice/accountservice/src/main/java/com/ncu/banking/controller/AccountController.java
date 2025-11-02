package com.ncu.banking.controller;

import com.ncu.banking.dto.AccountDto;
import com.ncu.banking.dto.TransactionDto;
import com.ncu.banking.exception.AccountNotFoundException;
import com.ncu.banking.exception.DatabaseException;
import com.ncu.banking.model.Account;
import com.ncu.banking.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;


@RestController
@RequestMapping(value = "/accounts", produces = "application/json")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/")
    public ResponseEntity<?> getAllAccounts() {
        try {
            List<Account> accounts = accountService.getAllAccounts();
            return ResponseEntity.ok(accounts);
        } catch (DatabaseException ex) {
            return ResponseEntity.status(503).body("Database error: " + ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Error: " + ex.getMessage());
        }
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<?> getAccountById(@PathVariable String accountId) {
        try {
            Account account = accountService.getAccountById(accountId);
            if (account == null) throw new AccountNotFoundException(accountId);
            return ResponseEntity.ok(account);
        } catch (AccountNotFoundException ex) {
            return ResponseEntity.status(404).body(ex.getMessage());
        } catch (DatabaseException ex) {
            return ResponseEntity.status(503).body("Database error: " + ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Error: " + ex.getMessage());
        }
    }

    @PostMapping("/")
    public ResponseEntity<?> createAccount(@RequestBody Account account) {
        try {
            Account created = accountService.addAccount(account);
            return ResponseEntity.ok(created);
        } catch (DatabaseException ex) {
            return ResponseEntity.status(503).body("Database error: " + ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("Failed to create account: " + ex.getMessage());
        }
    }

    @PutMapping("/{accountId}")
    public ResponseEntity<?> updateAccount(@PathVariable String accountId,
                                           @RequestBody Account updatedAccount) {
        try {
            boolean updated = accountService.updateAccount(accountId, updatedAccount);
            if (!updated) throw new AccountNotFoundException(accountId);
            return ResponseEntity.ok("Account updated successfully");
        } catch (AccountNotFoundException ex) {
            return ResponseEntity.status(404).body(ex.getMessage());
        } catch (DatabaseException ex) {
            return ResponseEntity.status(503).body("Database error: " + ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Error: " + ex.getMessage());
        }
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<?> deleteAccount(@PathVariable String accountId) {
        try {
            boolean deleted = accountService.deleteAccount(accountId);
            if (!deleted) throw new AccountNotFoundException(accountId);
            return ResponseEntity.ok("Account deleted successfully");
        } catch (AccountNotFoundException ex) {
            return ResponseEntity.status(404).body(ex.getMessage());
        } catch (DatabaseException ex) {
            return ResponseEntity.status(503).body("Database error: " + ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Error: " + ex.getMessage());
        }
    }

    @GetMapping("/{accountId}/transactions")
    public ResponseEntity<?> getTransactionsForAccount(@PathVariable String accountId) {
        try {
            Object response = accountService.getTransactionsForAccount(accountId);
            if (response == null) throw new AccountNotFoundException(accountId);
            return ResponseEntity.ok(response);
        } catch (AccountNotFoundException ex) {
            return ResponseEntity.status(404).body(ex.getMessage());
        } catch (DatabaseException ex) {
            return ResponseEntity.status(503).body("Database error: " + ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Error: " + ex.getMessage());
        }
    }

    @PostMapping("/{accountId}/transactions")
    public ResponseEntity<?> createTransaction(@PathVariable String accountId,
                                               @RequestBody TransactionDto transactionRequest) {
        try {
            transactionRequest.setAccountId(accountId);
            Object created = accountService.createTransaction(transactionRequest);
            return ResponseEntity.ok(created);
        } catch (AccountNotFoundException ex) {
            return ResponseEntity.status(404).body(ex.getMessage());
        } catch (DatabaseException ex) {
            return ResponseEntity.status(503).body("Database error: " + ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("Failed to create transaction: " + ex.getMessage());
        }
    }

    @GetMapping("/nextpage")
    public ResponseEntity<?> getAccountsNextPage(@RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "5") int size) {
        try {
            AccountService.PaginatedAccounts response = accountService.getAccountsByPage(page, size);
            return ResponseEntity.ok(response);
        } catch (DatabaseException ex) {
            return ResponseEntity.status(503).body("Database error: " + ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Error: " + ex.getMessage());
        }
    }

    @GetMapping("/customers/{customerId}/accounts")
    public ResponseEntity<?> getAccountsByCustomerId(@PathVariable String customerId) {
        try {
            List<Account> accounts = accountService.getAccountsByCustomerId(customerId);
            if (accounts == null || accounts.isEmpty()) return ResponseEntity.ok(Collections.emptyList());

            List<AccountDto> accountDtos = accounts.stream()
                    .map(acc -> new AccountDto(
                            acc.getAccountId(),
                            acc.getAccountHolderName(),
                            acc.getBalance(),
                            acc.getAccountType()
                    ))
                    .toList();

            return ResponseEntity.ok(accountDtos);

        } catch (DatabaseException ex) {
            return ResponseEntity.status(503).body("Database error: " + ex.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Collections.emptyList());
        }
    }
}
