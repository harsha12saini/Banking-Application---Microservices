package com.ncu.banking.service;

import com.ncu.banking.dto.TransactionDto;
import com.ncu.banking.exception.AccountNotFoundException;
import com.ncu.banking.exception.DatabaseException;
import com.ncu.banking.exception.TransactionNotFoundException;
import com.ncu.banking.irepository.iTransactionRepository;
import com.ncu.banking.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private final iTransactionRepository transactionRepository;

    @Autowired
    public TransactionService(iTransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    // -------------------- Get all transactions --------------------
    public List<TransactionDto> getAllTransactions() {
        try {
            List<Transaction> transactions = transactionRepository.getAllTransactions();
            return transactions.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            throw new DatabaseException("Failed to fetch all transactions", ex);
        }
    }

    // -------------------- Get transaction by ID --------------------
    public TransactionDto getTransactionById(String transactionId) {
        try {
            Transaction transaction = transactionRepository.getTransactionById(transactionId);
            if (transaction == null) {
                throw new TransactionNotFoundException("Transaction ID " + transactionId + " not found");
            }
            return convertToDto(transaction);
        } catch (TransactionNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new DatabaseException("Failed to fetch transaction by ID", ex);
        }
    }

    // -------------------- Add transaction --------------------
    public TransactionDto addTransaction(Transaction transaction) {
        try {
            Transaction created = transactionRepository.addTransaction(transaction);
            if (created == null) {
                throw new DatabaseException("Failed to create transaction", null);
            }
            return convertToDto(created);
        } catch (Exception ex) {
            throw new DatabaseException("Failed to create transaction", ex);
        }
    }

    // -------------------- Delete transaction --------------------
    public boolean deleteTransaction(String transactionId) {
        try {
            boolean deleted = transactionRepository.deleteTransaction(transactionId);
            if (!deleted) {
                throw new TransactionNotFoundException("Transaction ID " + transactionId + " not found");
            }
            return true;
        } catch (TransactionNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new DatabaseException("Failed to delete transaction", ex);
        }
    }

    // -------------------- Get transactions by Account ID --------------------
    public List<TransactionDto> getTransactionsByAccountId(String accountId) {
        try {
            List<Transaction> transactions = transactionRepository.getTransactionsByAccountId(accountId);
            if (transactions == null || transactions.isEmpty()) {
                throw new AccountNotFoundException("No transactions found for account ID " + accountId);
            }
            return transactions.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
        } catch (AccountNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new DatabaseException("Failed to fetch transactions for account", ex);
        }
    }

    // -------------------- Pagination --------------------
    public PaginatedTransactions getTransactionsByPage(int page, int size) {
        try {
            List<Transaction> transactions = transactionRepository.GetTransactionsByPage(page, size);
            int totalElements = transactionRepository.getTransactionCount();
            int totalPages = (int) Math.ceil((double) totalElements / size);

            PaginatedTransactions response = new PaginatedTransactions();
            response.setPage(page);
            response.setSize(size);
            response.setTotalPages(totalPages);
            response.setTotalElements(totalElements);
            response.setTransactions(
                    transactions.stream()
                            .map(this::convertToDto)
                            .collect(Collectors.toList())
            );
            return response;
        } catch (Exception ex) {
            throw new DatabaseException("Failed to fetch paginated transactions", ex);
        }
    }

    // -------------------- Helper: Entity → DTO --------------------
    private TransactionDto convertToDto(Transaction t) {
        return new TransactionDto(
                t.getTransactionId(),
                t.getAccountId(),
                t.getAmount(),
                t.getType(),
                t.getTimestamp()
        );
    }

    // -------------------- Inner static class for pagination --------------------
    public static class PaginatedTransactions {
        private List<TransactionDto> transactions;
        private int page;
        private int size;
        private int totalPages;
        private int totalElements;

        public PaginatedTransactions() {}

        public PaginatedTransactions(List<TransactionDto> transactions, int page, int size, int totalPages, int totalElements) {
            this.transactions = transactions;
            this.page = page;
            this.size = size;
            this.totalPages = totalPages;
            this.totalElements = totalElements;
        }

        // Getters and Setters
        public List<TransactionDto> getTransactions() {
            return transactions;
        }

        public void setTransactions(List<TransactionDto> transactions) {
            this.transactions = transactions;
        }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getTotalPages() {
            return totalPages;
        }

        public void setTotalPages(int totalPages) {
            this.totalPages = totalPages;
        }

        public int getTotalElements() {
            return totalElements;
        }

        public void setTotalElements(int totalElements) {
            this.totalElements = totalElements;
        }
    }
}
