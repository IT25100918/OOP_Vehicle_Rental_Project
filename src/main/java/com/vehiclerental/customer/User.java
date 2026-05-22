package com.vehiclerental.customer;

import com.vehiclerental.shared.FieldCodec;

public class User {

    private String userId;
    private String fullName;
    private String email;
    private String password;
    private String phoneNumber;
    private String licenceNumber;
    private String role;

    public User() {}

    public User(String userId, String fullName, String email, String password,
                String phoneNumber, String licenceNumber, String role) {
        this.userId        = userId;
        this.fullName      = fullName;
        this.email         = email;
        this.password      = password;
        this.phoneNumber   = phoneNumber;
        this.licenceNumber = licenceNumber;
        this.role          = role;
    }

    public String getUserId()        { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getFullName()      { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail()         { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword()      { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getPhoneNumber()   { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getLicenceNumber() { return licenceNumber; }
    public void setLicenceNumber(String licenceNumber) { this.licenceNumber = licenceNumber; }
    public String getRole()          { return role; }
    public void setRole(String role) { this.role = role; }

    public String toFileString() {
        return FieldCodec.encode(userId) + "," +
               FieldCodec.encode(fullName) + "," +
               FieldCodec.encode(email) + "," +
               FieldCodec.encode(password) + "," +
               FieldCodec.encode(phoneNumber) + "," +
               FieldCodec.encode(licenceNumber) + "," +
               FieldCodec.encode(role);
    }

    public static User fromFileString(String line) {
        String[] p = line.trim().split(",", 7);
        return new User(
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
