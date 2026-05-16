package com.vehiclerental.model;

// Encapsulation: All fields are private, accessed via getters/setters
public class User {
    private String userId;
    private String name;
    private String nic;
    private String contact;
    private String email;
    private String password;
    private String userType; // "admin" or "customer"

    public User() {}

    public User(String userId, String name, String nic, String contact, String email, String password, String userType) {
        this.userId = userId;
        this.name = name;
        this.nic = nic;
        this.contact = contact;
        this.email = email;
        this.password = password;
        this.userType = userType;
    }

    // Getters and Setters (Encapsulation)
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getNic() { return nic; }
    public void setNic(String nic) { this.nic = nic; }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getUserType() { return userType; }
    public void setUserType(String userType) { this.userType = userType; }

    // Convert to CSV string for file storage
    public String toCsv() {
        return userId + "," + name + "," + nic + "," + contact + "," + email + "," + password + "," + userType;
    }

    // Parse from CSV string
    public static User fromCsv(String csv) {
        String[] parts = csv.split(",", -1);
        if (parts.length < 7) return null;
        return new User(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], parts[6]);
    }

    @Override
    public String toString() {
        return "User{id=" + userId + ", name=" + name + ", type=" + userType + "}";
    }
}
