package com.vehiclerental.model;

// Encapsulation: Review class secures feedback data
public class Feedback {
    private String feedbackId;
    private String userId;
    private String vehicleId;
    private String userName;
    private String vehicleInfo;
    private int rating;       // 1-5
    private String comment;
    private String type;      // "vehicle" or "service"
    private String status;    // "active", "hidden"
    private String createdAt;

    public Feedback() {}

    public Feedback(String feedbackId, String userId, String vehicleId, String userName,
                    String vehicleInfo, int rating, String comment, String type,
                    String status, String createdAt) {
        this.feedbackId = feedbackId;
        this.userId = userId;
        this.vehicleId = vehicleId;
        this.userName = userName;
        this.vehicleInfo = vehicleInfo;
        this.rating = rating;
        this.comment = comment;
        this.type = type;
        this.status = status;
        this.createdAt = createdAt;
    }

    // Getters & Setters
    public String getFeedbackId() { return feedbackId; }
    public void setFeedbackId(String feedbackId) { this.feedbackId = feedbackId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getVehicleId() { return vehicleId; }
    public void setVehicleId(String vehicleId) { this.vehicleId = vehicleId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getVehicleInfo() { return vehicleInfo; }
    public void setVehicleInfo(String vehicleInfo) { this.vehicleInfo = vehicleInfo; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    // Returns star string for display
    public String getStars() {
        return "★".repeat(rating) + "☆".repeat(5 - rating);
    }

    public String toCsv() {
        return feedbackId + "," + userId + "," + vehicleId + "," + userName + "," +
               vehicleInfo + "," + rating + "," + comment.replace(",", ";") + "," +
               type + "," + status + "," + createdAt;
    }

    public static Feedback fromCsv(String csv) {
        String[] parts = csv.split(",", -1);
        if (parts.length < 10) return null;
        return new Feedback(parts[0], parts[1], parts[2], parts[3], parts[4],
                            Integer.parseInt(parts[5]), parts[6].replace(";", ","),
                            parts[7], parts[8], parts[9]);
    }
}
