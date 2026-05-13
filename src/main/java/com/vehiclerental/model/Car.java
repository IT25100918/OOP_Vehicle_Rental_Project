package com.vehiclerental.model;

// Inheritance: Car extends Vehicle
public class Car extends Vehicle {
    private int seats;
    private String fuelType;

    public Car() { super(); this.setType("car"); }

    public Car(String vehicleId, String brand, String model, String plateNumber,
               double rentPrice, String availability, String imageUrl, int seats, String fuelType) {
        super(vehicleId, "car", brand, model, plateNumber, rentPrice, availability, imageUrl);
        this.seats = seats;
        this.fuelType = fuelType;
    }

    public int getSeats() { return seats; }
    public void setSeats(int seats) { this.seats = seats; }

    public String getFuelType() { return fuelType; }
    public void setFuelType(String fuelType) { this.fuelType = fuelType; }

    // Polymorphism: Override display info
    @Override
    public String getDisplayInfo() {
        return getBrand() + " " + getModel() + " | " + seats + " seats | " + fuelType + " | Rs. " + getRentPrice() + "/day";
    }
}
