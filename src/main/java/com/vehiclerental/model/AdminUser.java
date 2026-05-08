package com.vehiclerental.model;

// Inheritance: AdminUser extends User
public class AdminUser extends User {
    private String role;
    private String permissions;

    public AdminUser() {
        super();
        this.setUserType("admin");
    }

    public AdminUser(String userId, String name, String nic, String contact, String email, String password, String role, String permissions) {
        super(userId, name, nic, contact, email, password, "admin");
        this.role = role;
        this.permissions = permissions;
    }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getPermissions() { return permissions; }
    public void setPermissions(String permissions) { this.permissions = permissions; }

    // Polymorphism: Override authentication check for admin
    public boolean hasPermission(String action) {
        if (permissions == null) return false;
        return permissions.contains(action) || permissions.equals("ALL");
    }

    @Override
    public String toCsv() {
        return super.toCsv() + "," + role + "," + permissions;
    }
}
