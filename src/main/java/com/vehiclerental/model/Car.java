package com.vehiclerental.model;

/**
 * Car — inherits from Vehicle (INHERITANCE).
 * Overrides getDisplayInfo() for POLYMORPHISM.
 */
public class Car extends Vehicle {

    public Car() {
        super();
        setType("CAR");
    }

    public Car(String id, String brand, String model, int year,
               double rentalPricePerDay, boolean available, String description) {
        super(id, brand, model, year, rentalPricePerDay, available, description, "CAR");
    }

    @Override
    public String getDisplayInfo() {
        return String.format("🚗 CAR | %s %s (%d) | LKR %.2f/day | %s",
                getBrand(), getModel(), getYear(),
                getRentalPricePerDay(),
                isAvailable() ? "Available" : "Unavailable");
    }
}
