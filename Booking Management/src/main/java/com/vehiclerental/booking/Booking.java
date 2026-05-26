package com.vehiclerental.booking;

import com.vehiclerental.shared.FieldCodec;

public class Booking {

    private String bookingId;
    private String userId;
    private String vehicleId;
    private String userName;
    private String vehicleName;
    private String startDate;
    private String endDate;
    private String status;
    private double totalPrice;

    public Booking() {}

    public Booking(String bookingId, String userId, String vehicleId,
                   String userName, String vehicleName, String startDate,
                   String endDate, String status, double totalPrice) {
        this.bookingId   = bookingId;
        this.userId      = userId;
        this.vehicleId   = vehicleId;
        this.userName    = userName;
        this.vehicleName = vehicleName;
        this.startDate   = startDate;
        this.endDate     = endDate;
        this.status      = status;
        this.totalPrice  = totalPrice;
    }

    public String getBookingId()   { return bookingId; }
    public void setBookingId(String bookingId) { this.bookingId = bookingId; }
    
    public String getUserId()      { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    
    public String getVehicleId()   { return vehicleId; }
    public void setVehicleId(String vehicleId) { this.vehicleId = vehicleId; }
    
    public String getUserName()    { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    
    public String getVehicleName() { return vehicleName; }
    public void setVehicleName(String vehicleName) { this.vehicleName = vehicleName; }
    
    public String getStartDate()   { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }
    
    public String getEndDate()     { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }
    
    public String getStatus()      { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public double getTotalPrice()  { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }

    /**
     * Converts the Booking object into a single line string
     * so it can be stored inside a text file.
     * FieldCodec.encode() is used to safely store special characters.
     */
    public String toFileString() {
        return FieldCodec.encode(bookingId) + "," +
               FieldCodec.encode(userId) + "," +
               FieldCodec.encode(vehicleId) + "," +
               FieldCodec.encode(userName) + "," +
               FieldCodec.encode(vehicleName) + "," +
               FieldCodec.encode(startDate) + "," +
               FieldCodec.encode(endDate) + "," +
               FieldCodec.encode(status) + "," +
               totalPrice;
    }
// Split the line into parts using commas
    public static Booking fromFileString(String line) {
        String[] p = line.split(",", 9);

         // Create and return a new Booking object
        return new Booking(
            FieldCodec.decode(p[0]), FieldCodec.decode(p[1]),
            FieldCodec.decode(p[2]), FieldCodec.decode(p[3]),
            FieldCodec.decode(p[4]), FieldCodec.decode(p[5]),
            FieldCodec.decode(p[6]), FieldCodec.decode(p[7]),
            Double.parseDouble(p[8])
        );
    }
}
