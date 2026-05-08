package com.vehiclerental.model;

// Inheritance: Van extends Vehicle
public class Van extends Vehicle {
    private int capacity; // passenger/cargo capacity
    private boolean hasAC;

    public Van() { super(); this.setType("van"); }

    public Van(String vehicleId, String brand, String model, String plateNumber,
               double rentPrice, String availability, String imageUrl,
               int capacity, boolean hasAC) {
        super(vehicleId, "van", brand, model, plateNumber, rentPrice, availability, imageUrl);
        this.capacity = capacity;
        this.hasAC = hasAC;
    }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public boolean isHasAC() { return hasAC; }
    public void setHasAC(boolean hasAC) { this.hasAC = hasAC; }

    // Polymorphism: Override display info
    @Override
    public String getDisplayInfo() {
        return getBrand() + " " + getModel() + " | " + capacity + " seats | " + (hasAC ? "AC" : "No AC") + " | Rs. " + getRentPrice() + "/day";
    }
}
