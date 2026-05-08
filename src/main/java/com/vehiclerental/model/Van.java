package com.vehiclerental.model;

/**
 * Van — inherits from Vehicle (INHERITANCE).
 * Overrides getDisplayInfo() for POLYMORPHISM.
 */
public class Van extends Vehicle {

    public Van() {
        super();
        setType("VAN");
    }

    public Van(String id, String brand, String model, int year,
               double rentalPricePerDay, boolean available, String description) {
        super(id, brand, model, year, rentalPricePerDay, available, description, "VAN");
    }

    @Override
    public String getDisplayInfo() {
        return String.format("🚐 VAN | %s %s (%d) | LKR %.2f/day | %s",
                getBrand(), getModel(), getYear(),
                getRentalPricePerDay(),
                isAvailable() ? "Available" : "Unavailable");
    }
}
