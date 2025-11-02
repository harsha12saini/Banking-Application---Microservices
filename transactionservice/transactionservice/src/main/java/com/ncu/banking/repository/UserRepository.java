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

    // ✅ Add a new user
    public boolean addUser(User user) {
        try {
            String sql = "INSERT INTO users (u_name, u_email, u_password) VALUES (?, ?, ?)";
            jdbcTemplate.update(sql, user.get_UserName(), user.get_Name(), user.get_Password());
            return true;
        } catch (Exception e) {
            System.out.println("Error inserting user: " + e.getMessage());
            return false;
        }
    }

    // ✅ Fetch a single user by email
    public User getUserByEmail(String email) {
        try {
            String sql = "SELECT * FROM users WHERE u_email = ?";
            return jdbcTemplate.queryForObject(sql, this::mapUser, email);
        } catch (Exception e) {
            System.out.println("Error fetching user: " + e.getMessage());
            return null;
        }
    }

    // ✅ Fetch all users
    public List<User> getAllUsers() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, this::mapUser);
    }

    // ✅ Helper method to map result to User object
    private User mapUser(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.set_UserName(rs.getString("u_name"));
        user.set_Name(rs.getString("u_email"));
        user.set_Password(rs.getString("u_password"));
        return user;
    }

    // ✅ Delete user
    public boolean deleteUserByEmail(String email) {
        try {
            String sql = "DELETE FROM users WHERE u_email = ?";
            int rows = jdbcTemplate.update(sql, email);
            return rows > 0;
        } catch (Exception e) {
            System.out.println("Error deleting user: " + e.getMessage());
            return false;
        }
    }
}
