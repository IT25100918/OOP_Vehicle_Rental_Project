package com.vehiclerental.model;

public class OnlinePayment extends Payment {

    private String transactionId;

    public OnlinePayment() {
        super();
        setPaymentMethod("Online");
    }

    public OnlinePayment(String paymentId, String bookingId, String userId,
                         String userName, String vehicleName, double amount,
                         String paymentDate, String status, String transactionId) {
        super(paymentId, bookingId, userId, userName, vehicleName,
              amount, paymentDate, "Online", status);
        this.transactionId = transactionId;
    }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    @Override
    public String getPaymentMethod() { return "Online"; }
}
