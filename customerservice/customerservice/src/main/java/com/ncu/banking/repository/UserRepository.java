package com.ncu.banking.repository;

import com.ncu.banking.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Add a new user
    public boolean addUser(User user) {
        try {
            String sql = "INSERT INTO users (u_name, u_email, u_password) VALUES (?, ?, ?)";
            jdbcTemplate.update(sql, user.get_Name(), user.get_UserName(), user.get_Password());
            return true;
        } catch (Exception e) {
            System.err.println("Error inserting user: " + e.getMessage());
            return false;
        }
    }

    // Fetch a single user by email (used for authentication)
    public User getUserByEmail(String email) {
        try {
            String sql = "SELECT * FROM users WHERE u_email = ?";
            return jdbcTemplate.queryForObject(sql, this::mapUser, email);
        } catch (Exception e) {
            System.err.println("Error fetching user by email: " + e.getMessage());
            return null;
        }
    }

    // Fetch all users
    public List<User> getAllUsers() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, this::mapUser);
    }

    // Map result set to User object
    private User mapUser(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.set_UserName(rs.getString("u_email")); // email is username for login
        user.set_Name(rs.getString("u_name"));
        user.set_Password(rs.getString("u_password"));
        return user;
    }

    // Delete user
    public boolean deleteUserByEmail(String email) {
        try {
            int rows = jdbcTemplate.update("DELETE FROM users WHERE u_email = ?", email);
            return rows > 0;
        } catch (Exception e) {
            System.err.println("Error deleting user: " + e.getMessage());
            return false;
        }
    }
}

