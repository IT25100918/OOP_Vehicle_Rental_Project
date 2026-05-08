package com.vehiclerental.model;

// Inheritance: RegularUser extends User
public class RegularUser extends User {
    private String licenceNo;
    private String membershipType; // "basic" or "premium"

    public RegularUser() {
        super();
        this.setUserType("customer");
    }

    public RegularUser(String userId, String name, String nic, String contact, String email, String password, String licenceNo, String membershipType) {
        super(userId, name, nic, contact, email, password, "customer");
        this.licenceNo = licenceNo;
        this.membershipType = membershipType;
    }

    public String getLicenceNo() { return licenceNo; }
    public void setLicenceNo(String licenceNo) { this.licenceNo = licenceNo; }

    public String getMembershipType() { return membershipType; }
    public void setMembershipType(String membershipType) { this.membershipType = membershipType; }

    @Override
    public String toCsv() {
        return super.toCsv() + "," + licenceNo + "," + membershipType;
    }

    public static RegularUser fromCsv(String csv) {
        String[] parts = csv.split(",", -1);
        if (parts.length < 9) return null;
        return new RegularUser(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], parts[7], parts[8]);
    }
}
