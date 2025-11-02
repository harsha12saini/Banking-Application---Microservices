package com.ncu.banking.irepository;

import java.util.List;
import com.ncu.banking.model.Account;

public interface iAccountRepository {

    List<Account> GetAllAccounts();
    Account GetAccountById(String accountId);
    Account GetAccountByName(String accountName);
    Account AddAccount(Account account);
    Integer GetAccountCount();
    String GetAccountNameById(String accountId);
    String GetAccountIdByName(String accountName);
    List<Account> GetAccountsByCustomerId(String customerId);

    // 🔥 New methods for update & delete
    Account UpdateAccount(Account account);
    boolean DeleteAccount(String accountId);
    List<Account> GetAccountsByPage(int page, int size);
   

}
