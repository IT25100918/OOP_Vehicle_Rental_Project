package com.vehiclerental.model;

// Encapsulation: Payment class encapsulates transaction data
public class Payment {
    private String paymentId;
    private String bookingId;
    private String userId;
    private String userName;
    private double amount;
    private double lateFee;
    private double totalAmount;
    private String paymentMethod; // "cash" or "online"
    private String status;        // "pending", "paid", "overdue"
    private String paymentDate;

    public Payment() {}

    public Payment(String paymentId, String bookingId, String userId, String userName,
                   double amount, double lateFee, double totalAmount,
                   String paymentMethod, String status, String paymentDate) {
        this.paymentId = paymentId;
        this.bookingId = bookingId;
        this.userId = userId;
        this.userName = userName;
        this.amount = amount;
        this.lateFee = lateFee;
        this.totalAmount = totalAmount;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.paymentDate = paymentDate;
    }

    // Getters & Setters
    public String getPaymentId() { return paymentId; }
    public void setPaymentId(String paymentId) { this.paymentId = paymentId; }

    public String getBookingId() { return bookingId; }
    public void setBookingId(String bookingId) { this.bookingId = bookingId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public double getLateFee() { return lateFee; }
    public void setLateFee(double lateFee) { this.lateFee = lateFee; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getPaymentDate() { return paymentDate; }
    public void setPaymentDate(String paymentDate) { this.paymentDate = paymentDate; }

    // Polymorphism: Calculate processing fee differently by payment method
    public double getProcessingFee() {
        if ("online".equalsIgnoreCase(paymentMethod)) {
            return amount * 0.02; // 2% for online
        }
        return 0; // No fee for cash
    }

    public String toCsv() {
        return paymentId + "," + bookingId + "," + userId + "," + userName + "," +
               amount + "," + lateFee + "," + totalAmount + "," +
               paymentMethod + "," + status + "," + paymentDate;
    }

    public static Payment fromCsv(String csv) {
        String[] parts = csv.split(",", -1);
        if (parts.length < 10) return null;
        return new Payment(parts[0], parts[1], parts[2], parts[3],
                           Double.parseDouble(parts[4]), Double.parseDouble(parts[5]),
                           Double.parseDouble(parts[6]), parts[7], parts[8], parts[9]);
    }
}
