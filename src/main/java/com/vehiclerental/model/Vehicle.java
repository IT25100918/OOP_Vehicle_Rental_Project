package com.vehiclerental.model;

// Encapsulation: Vehicle base class
public class Vehicle {
    private String vehicleId;
    private String type;       // "car", "bike", "van"
    private String brand;
    private String model;
    private String plateNumber;
    private double rentPrice;  // per day
    private String availability; // "available" or "rented"
    private String imageUrl;
    private String location;   // "lat,lng" e.g. "6.9271,79.8612"

    public Vehicle() {}

    public Vehicle(String vehicleId, String type, String brand, String model,
                   String plateNumber, double rentPrice, String availability, String imageUrl) {
        this.vehicleId = vehicleId;
        this.type = type;
        this.brand = brand;
        this.model = model;
        this.plateNumber = plateNumber;
        this.rentPrice = rentPrice;
        this.availability = availability;
        this.imageUrl = imageUrl;
        this.location = "";
    }

    public Vehicle(String vehicleId, String type, String brand, String model,
                   String plateNumber, double rentPrice, String availability, String imageUrl, String location) {
        this(vehicleId, type, brand, model, plateNumber, rentPrice, availability, imageUrl);
        this.location = location != null ? location : "";
    }

    // Getters & Setters (Encapsulation)
    public String getVehicleId() { return vehicleId; }
    public void setVehicleId(String vehicleId) { this.vehicleId = vehicleId; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public String getPlateNumber() { return plateNumber; }
    public void setPlateNumber(String plateNumber) { this.plateNumber = plateNumber; }

    public double getRentPrice() { return rentPrice; }
    public void setRentPrice(double rentPrice) { this.rentPrice = rentPrice; }

    public String getAvailability() { return availability; }
    public void setAvailability(String availability) { this.availability = availability; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getLocation() { return location != null ? location : ""; }
    public void setLocation(String location) { this.location = location != null ? location : ""; }

    /** Returns latitude as double, or 0 if not set */
    public double getLat() {
        try { if (location != null && location.contains(",")) return Double.parseDouble(location.split(",")[0].trim()); } catch (Exception ignored) {}
        return 0;
    }

    /** Returns longitude as double, or 0 if not set */
    public double getLng() {
        try { if (location != null && location.contains(",")) return Double.parseDouble(location.split(",")[1].trim()); } catch (Exception ignored) {}
        return 0;
    }

    public boolean hasLocation() {
        return location != null && location.contains(",") && getLat() != 0;
    }

    public boolean isAvailable() {
        return "available".equalsIgnoreCase(availability);
    }

    // Polymorphism: Can be overridden to show different display info
    public String getDisplayInfo() {
        return brand + " " + model + " (" + type + ") - Rs. " + rentPrice + "/day";
    }

    public String toCsv() {
        return vehicleId + "," + type + "," + brand + "," + model + "," +
               plateNumber + "," + rentPrice + "," + availability + "," + imageUrl + "," + getLocation();
    }

    public static Vehicle fromCsv(String csv) {
        String[] parts = csv.split(",", -1);
        if (parts.length < 8) return null;
        String loc = parts.length >= 9 ? parts[8] + (parts.length >= 10 ? "," + parts[9] : "") : "";
        Vehicle v = new Vehicle(parts[0], parts[1], parts[2], parts[3], parts[4],
                           Double.parseDouble(parts[5]), parts[6], parts[7]);
        v.setLocation(loc);
        return v;
    }
}
