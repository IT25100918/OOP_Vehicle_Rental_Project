package com.vehiclerental.model;

import com.vehiclerental.util.FieldCodec;

public class Payment {

    private String paymentId;
    private String bookingId;
    private String userId;
    private String userName;
    private String vehicleName;
    private double amount;
    private String paymentDate;
    private String paymentMethod;
    private String status;

    public Payment() {}

    public Payment(String paymentId, String bookingId, String userId,
                   String userName, String vehicleName, double amount,
                   String paymentDate, String paymentMethod, String status) {
        this.paymentId     = paymentId;
        this.bookingId     = bookingId;
        this.userId        = userId;
        this.userName      = userName;
        this.vehicleName   = vehicleName;
        this.amount        = amount;
        this.paymentDate   = paymentDate;
        this.paymentMethod = paymentMethod;
        this.status        = status;
    }

    public String getPaymentId()     { return paymentId; }
    public void setPaymentId(String paymentId) { this.paymentId = paymentId; }
    public String getBookingId()     { return bookingId; }
    public void setBookingId(String bookingId) { this.bookingId = bookingId; }
    public String getUserId()        { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getUserName()      { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public String getVehicleName()   { return vehicleName; }
    public void setVehicleName(String vehicleName) { this.vehicleName = vehicleName; }
    public double getAmount()        { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public String getPaymentDate()   { return paymentDate; }
    public void setPaymentDate(String paymentDate) { this.paymentDate = paymentDate; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public String getStatus()        { return status; }
    public void setStatus(String status) { this.status = status; }

    public String toFileString() {
        return FieldCodec.encode(paymentId) + "," +
               FieldCodec.encode(bookingId) + "," +
               FieldCodec.encode(userId) + "," +
               FieldCodec.encode(userName) + "," +
               FieldCodec.encode(vehicleName) + "," +
               amount + "," +
               FieldCodec.encode(paymentDate) + "," +
               FieldCodec.encode(paymentMethod) + "," +
               FieldCodec.encode(status);
    }

    public static Payment fromFileString(String line) {
        String[] p = line.split(",", 9);
        return new Payment(
            FieldCodec.decode(p[0]), FieldCodec.decode(p[1]),
            FieldCodec.decode(p[2]), FieldCodec.decode(p[3]),
            FieldCodec.decode(p[4]), Double.parseDouble(p[5]),
            FieldCodec.decode(p[6]), FieldCodec.decode(p[7]),
            FieldCodec.decode(p[8])
        );
    }
}
