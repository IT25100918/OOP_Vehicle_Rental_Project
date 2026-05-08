package com.vehiclerental.model;

// Inheritance: VehicleReview extends Feedback
public class VehicleReview extends Feedback {
    private String condition;  // "excellent", "good", "fair", "poor"
    private boolean wouldRentAgain;

    public VehicleReview() {
        super();
        this.setType("vehicle");
    }

    public VehicleReview(String feedbackId, String userId, String vehicleId, String userName,
                         String vehicleInfo, int rating, String comment,
                         String status, String createdAt,
                         String condition, boolean wouldRentAgain) {
        super(feedbackId, userId, vehicleId, userName, vehicleInfo, rating, comment, "vehicle", status, createdAt);
        this.condition = condition;
        this.wouldRentAgain = wouldRentAgain;
    }

    public String getCondition() { return condition; }
    public void setCondition(String condition) { this.condition = condition; }

    public boolean isWouldRentAgain() { return wouldRentAgain; }
    public void setWouldRentAgain(boolean wouldRentAgain) { this.wouldRentAgain = wouldRentAgain; }

    // Polymorphism: Vehicle-specific display includes condition
    public String getSummary() {
        return getStars() + " | Condition: " + condition + " | Rent again: " + (wouldRentAgain ? "Yes" : "No");
    }
}
