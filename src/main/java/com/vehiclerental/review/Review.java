package com.vehiclerental.review;

import com.vehiclerental.shared.FieldCodec;

public class Review {

    private String reviewId;
    private String userId;
    private String vehicleId;
    private String userName;
    private String vehicleName;
    private int    rating;
    private String comment;
    private String reviewDate;
    private String status;

    public Review() {}

    public Review(String reviewId, String userId, String vehicleId,
                  String userName, String vehicleName, int rating,
                  String comment, String reviewDate, String status) {
        this.reviewId    = reviewId;
        this.userId      = userId;
        this.vehicleId   = vehicleId;
        this.userName    = userName;
        this.vehicleName = vehicleName;
        this.rating      = rating;
        this.comment     = comment;
        this.reviewDate  = reviewDate;
        this.status      = status;
    }

    public String getReviewId()    { return reviewId; }
    public void setReviewId(String reviewId) { this.reviewId = reviewId; }
    public String getUserId()      { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getVehicleId()   { return vehicleId; }
    public void setVehicleId(String vehicleId) { this.vehicleId = vehicleId; }
    public String getUserName()    { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public String getVehicleName() { return vehicleName; }
    public void setVehicleName(String vehicleName) { this.vehicleName = vehicleName; }
    public int getRating()         { return rating; }
    public void setRating(int rating) { this.rating = rating; }
    public String getComment()     { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public String getReviewDate()  { return reviewDate; }
    public void setReviewDate(String reviewDate) { this.reviewDate = reviewDate; }
    public String getStatus()      { return status; }
    public void setStatus(String status) { this.status = status; }

    public String toFileString() {
        return FieldCodec.encode(reviewId) + "," +
               FieldCodec.encode(userId) + "," +
               FieldCodec.encode(vehicleId) + "," +
               FieldCodec.encode(userName) + "," +
               FieldCodec.encode(vehicleName) + "," +
               rating + "," +
               FieldCodec.encode(comment) + "," +
               FieldCodec.encode(reviewDate) + "," +
               FieldCodec.encode(status);
    }

    public static Review fromFileString(String line) {
        String[] p = line.split(",", 9);
        return new Review(
            FieldCodec.decode(p[0]), FieldCodec.decode(p[1]),
            FieldCodec.decode(p[2]), FieldCodec.decode(p[3]),
            FieldCodec.decode(p[4]), Integer.parseInt(p[5]),
            FieldCodec.decode(p[6]), FieldCodec.decode(p[7]),
            FieldCodec.decode(p[8])
        );
    }
}
