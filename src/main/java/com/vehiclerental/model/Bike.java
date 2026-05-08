package com.vehiclerental.model;

/**
 * Bike — inherits from Vehicle (INHERITANCE).
 * Overrides getDisplayInfo() for POLYMORPHISM.
 */
public class Bike extends Vehicle {

    public Bike() {
        super();
        setType("BIKE");
    }

    public Bike(String id, String brand, String model, int year,
                double rentalPricePerDay, boolean available, String description) {
        super(id, brand, model, year, rentalPricePerDay, available, description, "BIKE");
    }

    @Override
    public String getDisplayInfo() {
        return String.format("🏍 BIKE | %s %s (%d) | LKR %.2f/day | %s",
                getBrand(), getModel(), getYear(),
                getRentalPricePerDay(),
                isAvailable() ? "Available" : "Unavailable");
    }
}
