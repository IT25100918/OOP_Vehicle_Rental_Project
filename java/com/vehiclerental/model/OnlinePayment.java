package com.vehiclerental.model;

// Inheritance: OnlinePayment extends Payment
public class OnlinePayment extends Payment {
    private String transactionRef; // online transaction reference number

    public OnlinePayment() {
        super();
        this.setPaymentMethod("online");
    }

    public OnlinePayment(String paymentId, String bookingId, String userId, String userName,
                         double amount, double lateFee, double totalAmount,
                         String status, String paymentDate, String transactionRef) {
        super(paymentId, bookingId, userId, userName, amount, lateFee, totalAmount, "online", status, paymentDate);
        this.transactionRef = transactionRef;
    }

    public String getTransactionRef() { return transactionRef; }
    public void setTransactionRef(String transactionRef) { this.transactionRef = transactionRef; }

    // Polymorphism: 2% processing fee for online payments
    @Override
    public double getProcessingFee() {
        return getAmount() * 0.02;
    }

    @Override
    public String toCsv() {
        return super.toCsv() + "," + (transactionRef != null ? transactionRef : "");
    }
}
