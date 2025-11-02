package com.ncu.banking.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ncu.banking.dto.SignupDto;
import com.ncu.banking.exceptions.DatabaseConnectionException;
import com.ncu.banking.exceptions.UserAlreadyExistsException;
import com.ncu.banking.exceptions.UserNotFoundException;

@Repository(value = "AuthRepository")
public class AuthRepository {

    private final JdbcTemplate _JdbcTemplate;

    @Autowired
    public AuthRepository(JdbcTemplate jdbcTemplate) {
        this._JdbcTemplate = jdbcTemplate;
    }

    public boolean signup(SignupDto cred, StringBuffer error) {
        try {
            // Check if user already exists
            String checkQuery = "SELECT COUNT(*) FROM users WHERE u_email = ?";
            Integer count = _JdbcTemplate.queryForObject(checkQuery, Integer.class, cred.get_Email());
            if (count != null && count > 0) {
                throw new UserAlreadyExistsException("User with email " + cred.get_Email() + " already exists.");
            }

            // Insert new user
            String insertQuery = "INSERT INTO users (u_name, u_email, u_password) VALUES (?, ?, ?)";
            _JdbcTemplate.update(insertQuery, cred.get_Name(), cred.get_Email(), cred.get_Password());
            return true;

        } catch (UserAlreadyExistsException ex) {
            error.append(ex.getMessage());
            throw ex;

        } catch (DataAccessException ex) {
            error.append("Database error: " + ex.getMessage());
            throw new DatabaseConnectionException("Database connection failed. Please try again later.");

        } catch (Exception ex) {
            error.append("Unexpected error: " + ex.getMessage());
            throw new RuntimeException("Unexpected error during signup.");
        }
    }

    public Boolean getPasswordFromEmail(String email, StringBuffer password, StringBuffer error) {
        try {
            String query = "SELECT u_password FROM users WHERE u_email = ?";
            String result = _JdbcTemplate.queryForObject(query, String.class, email);
            if (result == null || result.isEmpty()) {
                throw new UserNotFoundException("No user found with email: " + email);
            }

            password.append(result);
            return true;

        } catch (UserNotFoundException ex) {
            error.append(ex.getMessage());
            throw ex;

        } catch (DataAccessException ex) {
            error.append("Database error: " + ex.getMessage());
            throw new DatabaseConnectionException("Database not reachable or query failed.");

        } catch (Exception ex) {
            error.append("Unexpected error: " + ex.getMessage());
            throw new RuntimeException("Unexpected error during password retrieval.");
        }
    }
}
