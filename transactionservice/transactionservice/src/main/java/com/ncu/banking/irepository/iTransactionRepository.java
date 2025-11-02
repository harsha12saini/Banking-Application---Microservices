package com.ncu.banking.irepository;

import com.ncu.banking.model.Transaction;
import java.util.List;

public interface iTransactionRepository {
    List<Transaction> getAllTransactions();
    Transaction getTransactionById(String transactionId);
    List<Transaction> getTransactionsByAccountId(String accountId);
    Transaction addTransaction(Transaction transaction);
    Transaction updateTransaction(Transaction transaction);
    boolean deleteTransaction(String transactionId);
    Integer getTransactionCount();
    List<Transaction> GetTransactionsByPage(int page, int size);

}
