package com.ncu.banking.service;

import com.ncu.banking.dto.AccountDto;
import com.ncu.banking.dto.TransactionDto;
import com.ncu.banking.exception.DatabaseException;
import com.ncu.banking.model.Account;
import com.ncu.banking.irepository.iAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Service
public class AccountService {

    private final iAccountRepository accountRepository;
    private final RestTemplate restTemplate;

    @Value("${internal.api.key}")
    private String internalApiKey;

    @Autowired
    public AccountService(iAccountRepository accountRepository, RestTemplate restTemplate) {
        this.accountRepository = accountRepository;
        this.restTemplate = restTemplate;
    }

    // ---------------- CRUD ----------------
    public List<Account> getAllAccounts() {
        try {
            return accountRepository.GetAllAccounts();
        } catch (Exception e) {
            throw new DatabaseException("Error fetching all accounts", e);
        }
    }

    public Account getAccountById(String accountId) {
        try {
            return accountRepository.GetAccountById(accountId);
        } catch (Exception e) {
            throw new DatabaseException("Error fetching account with ID: " + accountId, e);
        }
    }

    public Account addAccount(Account account) {
        try {
            return accountRepository.AddAccount(account);
        } catch (Exception e) {
            throw new DatabaseException("Error adding new account", e);
        }
    }

    public boolean updateAccount(String accountId, Account updatedAccount) {
        try {
            Account existing = accountRepository.GetAccountById(accountId);
            if (existing == null) return false;

            existing.setAccountType(updatedAccount.getAccountType());
            existing.setBalance(updatedAccount.getBalance());
            existing.setCustomerId(updatedAccount.getCustomerId());

            accountRepository.UpdateAccount(existing);
            return true;
        } catch (Exception e) {
            throw new DatabaseException("Error updating account with ID: " + accountId, e);
        }
    }

    public boolean deleteAccount(String accountId) {
        try {
            return accountRepository.DeleteAccount(accountId);
        } catch (Exception e) {
            throw new DatabaseException("Error deleting account with ID: " + accountId, e);
        }
    }

    public List<Account> getAccountsByCustomerId(String customerId) {
        try {
            return accountRepository.GetAccountsByCustomerId(customerId);
        } catch (Exception e) {
            throw new DatabaseException("Error fetching accounts for customer ID: " + customerId, e);
        }
    }

    // ---------------- Transactions ----------------
    public AccountDto getTransactionsForAccount(String accountId) {
        Account account;
        try {
            account = accountRepository.GetAccountById(accountId);
            if (account == null) return null;
        } catch (Exception e) {
            throw new DatabaseException("Error fetching account with ID: " + accountId, e);
        }

        AccountDto accountDto = new AccountDto(
                account.getAccountId(),
                account.getAccountHolderName(),
                account.getBalance(),
                account.getAccountType()
        );

        try {
            String url = "http://localhost:3002/transactions/account/{accountId}";
            System.out.println("🌐 Calling TransactionService URL: " + url.replace("{accountId}", accountId));

            HttpHeaders headers = new HttpHeaders();
            headers.set("X-Internal-API-Key", internalApiKey);

            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<List<TransactionDto>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<List<TransactionDto>>() {},
                    accountId
            );

            System.out.println("✅ Response Status: " + response.getStatusCode());
            List<TransactionDto> transactions = response.getBody();
            System.out.println("📦 Transactions fetched: " + (transactions != null ? transactions.size() : 0));

            accountDto.setTransactions(transactions != null ? transactions : Collections.emptyList());
        } catch (Exception e) {
            System.out.println("⚠️ Error fetching transactions for " + accountId + ": " + e.getMessage());
            e.printStackTrace();
            accountDto.setTransactions(Collections.emptyList());
        }

        return accountDto;
    }

    // ---------------- Create Transaction ----------------
    public TransactionDto createTransaction(TransactionDto transactionRequest) {
        try {
            String url = "http://localhost:3002/transactions/";

            HttpHeaders headers = new HttpHeaders();
            headers.set("X-Internal-API-Key", internalApiKey);

            HttpEntity<TransactionDto> requestEntity = new HttpEntity<>(transactionRequest, headers);

            return restTemplate.postForObject(url, requestEntity, TransactionDto.class);
        } catch (Exception e) {
            throw new DatabaseException("Error creating transaction for account ID: " + transactionRequest.getAccountId(), e);
        }
    }

    // ---------------- Pagination ----------------
    public PaginatedAccounts getAccountsByPage(int page, int size) {
        try {
            List<Account> accounts = accountRepository.GetAccountsByPage(page, size);
            int totalElements = accountRepository.GetAccountCount();
            int totalPages = (int) Math.ceil((double) totalElements / size);

            PaginatedAccounts response = new PaginatedAccounts();
            response.setPage(page);
            response.setSize(size);
            response.setTotalPages(totalPages);
            response.setTotalElements(totalElements);
            response.setAccounts(accounts);

            return response;
        } catch (Exception e) {
            throw new DatabaseException("Error fetching paginated accounts", e);
        }
    }

    // ---------------- Inner Class ----------------
    public static class PaginatedAccounts {
        private int page;
        private int size;
        private int totalPages;
        private int totalElements;
        private List<Account> accounts;

        public int getPage() { return page; }
        public void setPage(int page) { this.page = page; }

        public int getSize() { return size; }
        public void setSize(int size) { this.size = size; }

        public int getTotalPages() { return totalPages; }
        public void setTotalPages(int totalPages) { this.totalPages = totalPages; }

        public int getTotalElements() { return totalElements; }
        public void setTotalElements(int totalElements) { this.totalElements = totalElements; }

        public List<Account> getAccounts() { return accounts; }
        public void setAccounts(List<Account> accounts) { this.accounts = accounts; }
    }
}
