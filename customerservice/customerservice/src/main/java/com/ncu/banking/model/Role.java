package com.ncu.banking.model;

public class Role {
    private long roleId;
    private String username; // references users.u_email
    private String role;

    public Role() {}

    public Role(String username, String role) {
        this.username = username;
        this.role = role;
    }

    public long getRoleId() { return roleId; }
    public void setRoleId(long roleId) { this.roleId = roleId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
