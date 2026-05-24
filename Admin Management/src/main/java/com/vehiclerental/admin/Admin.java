package com.vehiclerental.admin;

import com.vehiclerental.shared.FieldCodec;

public class Admin {

    private String adminId;
    private String fullName;
    private String email;
    private String password;
    private String phoneNumber;
    private String role;    // "SUPER_ADMIN" or "ADMIN"
    private String status;  // "Active" or "Inactive"

    public Admin() {}

    public Admin(String adminId, String fullName, String email, String password,
                 String phoneNumber, String role, String status) {
        this.adminId     = adminId;
        this.fullName    = fullName;
        this.email       = email;
        this.password    = password;
        this.phoneNumber = phoneNumber;
        this.role        = role;
        this.status      = status;
    }

    public String getAdminId()     { return adminId; }
    public void setAdminId(String adminId) { this.adminId = adminId; }
    public String getFullName()    { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail()       { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword()    { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getRole()        { return role; }
    public void setRole(String role) { this.role = role; }
    public String getStatus()      { return status; }
    public void setStatus(String status) { this.status = status; }

    public String toFileString() {
        return FieldCodec.encode(adminId) + "," +
               FieldCodec.encode(fullName) + "," +
               FieldCodec.encode(email) + "," +
               FieldCodec.encode(password) + "," +
               FieldCodec.encode(phoneNumber) + "," +
               FieldCodec.encode(role) + "," +
               FieldCodec.encode(status);
    }

    public static Admin fromFileString(String line) {
        String[] p = line.trim().split(",", 7);
        return new Admin(
            FieldCodec.decode(p[0].trim()),
            FieldCodec.decode(p[1].trim()),
            FieldCodec.decode(p[2].trim()),
            FieldCodec.decode(p[3].trim()),
            FieldCodec.decode(p[4].trim()),
            FieldCodec.decode(p[5].trim()),
            FieldCodec.decode(p[6].trim())
        );
    }
}
