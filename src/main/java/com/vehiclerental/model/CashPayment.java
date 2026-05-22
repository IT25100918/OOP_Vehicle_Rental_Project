package com.vehiclerental.model;

// Inheritance: CashPayment extends Payment
public class CashPayment extends Payment {
    private String receivedBy; // staff member who received cash

    public CashPayment() {
        super();
        this.setPaymentMethod("cash");
    }

    public CashPayment(String paymentId, String bookingId, String userId, String userName,
                       double amount, double lateFee, double totalAmount,
                       String status, String paymentDate, String receivedBy) {
        super(paymentId, bookingId, userId, userName, amount, lateFee, totalAmount, "cash", status, paymentDate);
        this.receivedBy = receivedBy;
    }

    public String getReceivedBy() { return receivedBy; }
    public void setReceivedBy(String receivedBy) { this.receivedBy = receivedBy; }

    // Polymorphism: No processing fee for cash payments
    @Override
    public double getProcessingFee() {
        return 0.0;
    }

    @Override
    public String toCsv() {
        return super.toCsv() + "," + (receivedBy != null ? receivedBy : "");
    }
}
