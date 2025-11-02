package com.ncu.banking.repository;

import com.ncu.banking.irepository.iAccountRepository;
import com.ncu.banking.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository("AccountRepositoryImpl")
public class AccountRepositoryImpl implements iAccountRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public AccountRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Account> GetAllAccounts() {
        try {
            return jdbcTemplate.query("SELECT * FROM account", new AccountRowMapper());
        } catch (Exception e) {
            System.out.println("Error fetching accounts: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public Account GetAccountById(String accountId) {
        try {
            String query = "SELECT * FROM account WHERE a_id = ?";
            return jdbcTemplate.queryForObject(query, new AccountRowMapper(), accountId);
        } catch (Exception e) {
            System.out.println("Error fetching account by ID: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Account GetAccountByName(String accountName) {
        try {
            String query = "SELECT * FROM account WHERE a_name = ?";
            return jdbcTemplate.queryForObject(query, new AccountRowMapper(), accountName);
        } catch (Exception e) {
            System.out.println("Error fetching account by name: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Account AddAccount(Account account) {
        try {
            String sqlIdGen = "SELECT IFNULL(MAX(CAST(SUBSTRING(a_id, 2) AS UNSIGNED)), 1000) + 1 FROM account";
            Integer nextId = jdbcTemplate.queryForObject(sqlIdGen, Integer.class);
            String accountId = "A" + nextId;
            account.setAccountId(accountId);

            String sql = "INSERT INTO account (a_id, a_name, a_email, a_password, a_balance, a_type, customer_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
            jdbcTemplate.update(sql,
                    account.getAccountId(),
                    account.getAccountHolderName(),
                    account.getEmail(),
                    account.getPassword(),
                    account.getBalance(),
                    account.getAccountType(),
                    account.getCustomerId()
            );
            return account;
        } catch (Exception e) {
            System.out.println("Error adding account: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Integer GetAccountCount() {
        try {
            return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM account", Integer.class);
        } catch (Exception e) {
            System.out.println("Error fetching account count: " + e.getMessage());
            return 0;
        }
    }

    @Override
    public String GetAccountNameById(String accountId) {
        try {
            return jdbcTemplate.queryForObject("SELECT a_name FROM account WHERE a_id = ?", String.class, accountId);
        } catch (Exception e) {
            System.out.println("Error fetching account name by ID: " + e.getMessage());
            return null;
        }
    }

    @Override
    public String GetAccountIdByName(String accountName) {
        try {
            return jdbcTemplate.queryForObject("SELECT a_id FROM account WHERE a_name = ?", String.class, accountName);
        } catch (Exception e) {
            System.out.println("Error fetching account ID by name: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Account UpdateAccount(Account account) {
        try {
            String sql = "UPDATE account SET a_name=?, a_email=?, a_password=?, a_balance=?, a_type=?, customer_id=? WHERE a_id=?";
            int rows = jdbcTemplate.update(sql,
                    account.getAccountHolderName(),
                    account.getEmail(),
                    account.getPassword(),
                    account.getBalance(),
                    account.getAccountType(),
                    account.getCustomerId(),
                    account.getAccountId()
            );
            return rows > 0 ? account : null;
        } catch (Exception e) {
            System.out.println("Error updating account: " + e.getMessage());
            return null;
        }
    }

    @Override
    public boolean DeleteAccount(String accountId) {
        try {
            int rows = jdbcTemplate.update("DELETE FROM account WHERE a_id = ?", accountId);
            return rows > 0;
        } catch (Exception e) {
            System.out.println("Error deleting account: " + e.getMessage());
            return false;
        }
    }

    // ✅ FIXED: Correct case-insensitive customerId match and proper column mapping
    @Override
public List<Account> GetAccountsByCustomerId(String customerId) {
    try {
        System.out.println("🔍 Fetching accounts for customerId='" + customerId + "'");
        String sql = "SELECT * FROM account WHERE TRIM(LOWER(customer_id)) = TRIM(LOWER(?))";
        List<Account> accounts = jdbcTemplate.query(sql, new AccountRowMapper(), customerId);
        System.out.println("✅ Found " + accounts.size() + " accounts for customerId=" + customerId);
        return accounts;
    } catch (Exception e) {
        System.out.println("❌ Error fetching accounts by customer ID: " + e.getMessage());
        return new ArrayList<>();
    }
}


    @Override
    public List<Account> GetAccountsByPage(int page, int size) {
        int offset = page * size;
        try {
            String sql = "SELECT * FROM account LIMIT ?, ?";
            return jdbcTemplate.query(sql, new AccountRowMapper(), offset, size);
        } catch (Exception e) {
            System.out.println("Error fetching accounts by page: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
