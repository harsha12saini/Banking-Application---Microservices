package com.ncu.banking.repository;

import com.ncu.banking.model.Transaction;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TransactionRowMapper implements RowMapper<Transaction> {
    @Override
    public Transaction mapRow(ResultSet rs, int rowNum) throws SQLException {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(rs.getString("transaction_id"));
        transaction.setAccountId(rs.getString("account_id"));
        transaction.setType(rs.getString("type"));
        transaction.setAmount(rs.getBigDecimal("amount"));
        transaction.setTimestamp(rs.getTimestamp("timestamp").toLocalDateTime());
        return transaction;
    }
}
