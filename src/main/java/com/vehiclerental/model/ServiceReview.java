package com.vehiclerental.model;

// Inheritance: ServiceReview extends Feedback
public class ServiceReview extends Feedback {
    private int staffRating;    // 1-5, separate rating for staff
    private String serviceType; // "pickup", "dropoff", "support"

    public ServiceReview() {
        super();
        this.setType("service");
    }

    public ServiceReview(String feedbackId, String userId, String vehicleId, String userName,
                         String vehicleInfo, int rating, String comment,
                         String status, String createdAt,
                         int staffRating, String serviceType) {
        super(feedbackId, userId, vehicleId, userName, vehicleInfo, rating, comment, "service", status, createdAt);
        this.staffRating = staffRating;
        this.serviceType = serviceType;
    }

    public int getStaffRating() { return staffRating; }
    public void setStaffRating(int staffRating) { this.staffRating = staffRating; }

    public String getServiceType() { return serviceType; }
    public void setServiceType(String serviceType) { this.serviceType = serviceType; }

    // Polymorphism: Service review shows staff rating as extra field
    public String getSummary() {
        return getStars() + " | Staff: " + staffRating + "/5 | Service: " + serviceType;
    }
}
