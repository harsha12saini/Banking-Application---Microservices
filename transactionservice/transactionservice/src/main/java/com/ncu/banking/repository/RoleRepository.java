package com.ncu.banking.repository;

import com.ncu.banking.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class RoleRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public RoleRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // ✅ Assign a role to a user
    public boolean assignRole(String username, String role) {
        try {
            String sql = "INSERT INTO roles (username, role) VALUES (?, ?)";
            jdbcTemplate.update(sql, username, role);
            return true;
        } catch (Exception e) {
            System.out.println("Error assigning role: " + e.getMessage());
            return false;
        }
    }

    // ✅ Get all roles of a user
    public List<Role> getRolesByUser(String username) {
        String sql = "SELECT * FROM roles WHERE username = ?";
        return jdbcTemplate.query(sql, this::mapRole, username);
    }

    private Role mapRole(ResultSet rs, int rowNum) throws SQLException {
        Role role = new Role();
        role.setRoleId(rs.getLong("role_id"));
        role.setUsername(rs.getString("username"));
        role.setRole(rs.getString("role"));
        return role;
    }
}
