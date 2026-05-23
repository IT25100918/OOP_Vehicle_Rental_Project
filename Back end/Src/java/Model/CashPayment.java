package com.vehiclerental.model;

public class CashPayment extends Payment {

    public CashPayment() {
        super();
        setPaymentMethod("Cash");
    }

    public CashPayment(String paymentId, String bookingId, String userId,
                       String userName, String vehicleName, double amount,
                       String paymentDate, String status) {
        super(paymentId, bookingId, userId, userName, vehicleName,
              amount, paymentDate, "Cash", status);
    }

    @Override
    public String getPaymentMethod() { return "Cash"; }
}
