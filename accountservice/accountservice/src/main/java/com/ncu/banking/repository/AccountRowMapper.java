package com.ncu.banking.repository;

import com.ncu.banking.model.Account;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountRowMapper implements RowMapper<Account> {

   @Override
public Account mapRow(ResultSet rs, int rowNum) throws SQLException {
    return new Account(
        rs.getString("a_id"),
        rs.getString("a_name"),
        rs.getString("a_email"),
        rs.getString("a_password"),
        rs.getDouble("a_balance"),
        rs.getString("a_type"),
        rs.getString("customer_id") // <-- read customer_id
    );
}

}
