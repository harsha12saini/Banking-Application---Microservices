package com.ncu.banking.repository;

import com.ncu.banking.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

@Repository
public class RoleRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public RoleRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Assign a role to a user
    public boolean assignRole(String username, String role) {
        try {
            String sql = "INSERT INTO roles (username, role) VALUES (?, ?)";
            int rows = jdbcTemplate.update(sql, username, role);
            return rows > 0;
        } catch (Exception e) {
            System.err.println("Error assigning role: " + e.getMessage());
            return false;
        }
    }

    // Get all roles of a user
    public List<Role> getRolesByUser(String username) {
        String sql = "SELECT * FROM roles WHERE username = ?";
        try {
            return jdbcTemplate.query(sql, this::mapRole, username);
        } catch (EmptyResultDataAccessException ex) {
            // Return empty list if user has no roles
            return Collections.emptyList();
        } catch (Exception ex) {
            System.err.println("Error fetching roles for user " + username + ": " + ex.getMessage());
            return Collections.emptyList();
        }
    }

    // Map result set to Role object
    private Role mapRole(ResultSet rs, int rowNum) throws SQLException {
        Role role = new Role();
        role.setRoleId(rs.getLong("role_id"));
        role.setUsername(rs.getString("username"));
        role.setRole(rs.getString("role"));
        return role;
    }
}
