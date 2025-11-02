package com.ncu.banking.repository;

import com.ncu.banking.irepository.iTransactionRepository;
import com.ncu.banking.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public class TransactionRepositoryImpl implements iTransactionRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TransactionRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Transaction> getAllTransactions() {
        String sql = "SELECT * FROM transactions";
        return jdbcTemplate.query(sql, new TransactionRowMapper());
    }

    @Override
    public Transaction getTransactionById(String transactionId) {
        String sql = "SELECT * FROM transactions WHERE transaction_id = ?";
        return jdbcTemplate.queryForObject(sql, new TransactionRowMapper(), transactionId);
    }

    @Override
    public List<Transaction> getTransactionsByAccountId(String accountId) {
        String sql = "SELECT * FROM transactions WHERE account_id = ?";
        return jdbcTemplate.query(sql, new TransactionRowMapper(), accountId);
    }

    @Override
    public Transaction addTransaction(Transaction transaction) {
        String sql = "INSERT INTO transactions (transaction_id, account_id, type, amount, timestamp) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                transaction.getTransactionId(),
                transaction.getAccountId(),
                transaction.getType(),
                transaction.getAmount(),
                Timestamp.valueOf(transaction.getTimestamp())
        );
        return transaction;
    }

    @Override
    public Transaction updateTransaction(Transaction transaction) {
        String sql = "UPDATE transactions SET account_id=?, type=?, amount=?, timestamp=? WHERE transaction_id=?";
        jdbcTemplate.update(sql,
                transaction.getAccountId(),
                transaction.getType(),
                transaction.getAmount(),
                Timestamp.valueOf(transaction.getTimestamp()),
                transaction.getTransactionId()
        );
        return transaction;
    }

    @Override
    public boolean deleteTransaction(String transactionId) {
        String sql = "DELETE FROM transactions WHERE transaction_id = ?";
        return jdbcTemplate.update(sql, transactionId) > 0;
    }

    @Override
    public Integer getTransactionCount() {
        String sql = "SELECT COUNT(*) FROM transactions";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    @Override
public List<Transaction> GetTransactionsByPage(int page, int size) {
    int offset = page * size;
    try {
        String sql = "SELECT * FROM transactions LIMIT ? OFFSET ?";
        List<Transaction> transactions = jdbcTemplate.query(sql, new TransactionRowMapper(), size, offset);
        System.out.println("Fetched " + transactions.size() + " transactions for page=" + page);
        return transactions;
    } catch (Exception e) {
        System.out.println("Error fetching transactions by page: " + e.getMessage());
        return List.of();
    }
}

}
