package com.vehiclerental.model;

// Encapsulation: Booking class manages all reservation data
public class Booking {
    private String bookingId;
    private String userId;
    private String vehicleId;
    private String userName;
    private String vehicleInfo;
    private String startDate;
    private String endDate;
    private int totalDays;
    private double totalCost;
    private String status;       // "pending", "confirmed", "cancelled", "completed"
    private String bookingType;  // "online" or "walkin"
    private String createdAt;

    public Booking() {}

    public Booking(String bookingId, String userId, String vehicleId, String userName,
                   String vehicleInfo, String startDate, String endDate, int totalDays,
                   double totalCost, String status, String bookingType, String createdAt) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.vehicleId = vehicleId;
        this.userName = userName;
        this.vehicleInfo = vehicleInfo;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalDays = totalDays;
        this.totalCost = totalCost;
        this.status = status;
        this.bookingType = bookingType;
        this.createdAt = createdAt;
    }

    // Getters & Setters
    public String getBookingId() { return bookingId; }
    public void setBookingId(String bookingId) { this.bookingId = bookingId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getVehicleId() { return vehicleId; }
    public void setVehicleId(String vehicleId) { this.vehicleId = vehicleId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getVehicleInfo() { return vehicleInfo; }
    public void setVehicleInfo(String vehicleInfo) { this.vehicleInfo = vehicleInfo; }

    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }

    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }

    public int getTotalDays() { return totalDays; }
    public void setTotalDays(int totalDays) { this.totalDays = totalDays; }

    public double getTotalCost() { return totalCost; }
    public void setTotalCost(double totalCost) { this.totalCost = totalCost; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getBookingType() { return bookingType; }
    public void setBookingType(String bookingType) { this.bookingType = bookingType; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String toCsv() {
        return bookingId + "," + userId + "," + vehicleId + "," + userName + "," +
               vehicleInfo + "," + startDate + "," + endDate + "," + totalDays + "," +
               totalCost + "," + status + "," + bookingType + "," + createdAt;
    }

    public static Booking fromCsv(String csv) {
        String[] parts = csv.split(",", -1);
        if (parts.length < 12) return null;
        return new Booking(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5],
                           parts[6], Integer.parseInt(parts[7]), Double.parseDouble(parts[8]),
                           parts[9], parts[10], parts[11]);
    }
}
