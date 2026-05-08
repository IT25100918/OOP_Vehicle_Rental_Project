package com.vehiclerental.model;

// Inheritance: Bike extends Vehicle
public class Bike extends Vehicle {
    private String bikeType; // "sport", "cruiser", "scooter"
    private int engineCC;

    public Bike() { super(); this.setType("bike"); }

    public Bike(String vehicleId, String brand, String model, String plateNumber,
                double rentPrice, String availability, String imageUrl,
                String bikeType, int engineCC) {
        super(vehicleId, "bike", brand, model, plateNumber, rentPrice, availability, imageUrl);
        this.bikeType = bikeType;
        this.engineCC = engineCC;
    }

    public String getBikeType() { return bikeType; }
    public void setBikeType(String bikeType) { this.bikeType = bikeType; }

    public int getEngineCC() { return engineCC; }
    public void setEngineCC(int engineCC) { this.engineCC = engineCC; }

    // Polymorphism: Override display info
    @Override
    public String getDisplayInfo() {
        return getBrand() + " " + getModel() + " | " + engineCC + "cc | " + bikeType + " | Rs. " + getRentPrice() + "/day";
    }
}
