package com.vehiclerental.model;

/**
 * Base Vehicle class — demonstrates ENCAPSULATION.
 * All fields are private; accessed via getters/setters.
 */
public abstract class Vehicle {

    private String id;
    private String brand;
    private String model;
    private int year;
    private double rentalPricePerDay;
    private boolean available;
    private String description;
    private String type; // "CAR", "VAN", "BIKE"

    public Vehicle() {}

    public Vehicle(String id, String brand, String model, int year,
                   double rentalPricePerDay, boolean available, String description, String type) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.rentalPricePerDay = rentalPricePerDay;
        this.available = available;
        this.description = description;
        this.type = type;
    }

    // POLYMORPHISM — subclasses override this to display differently
    public abstract String getDisplayInfo();

    // Converts vehicle to a pipe-delimited line for file storage
    public String toFileString() {
        return String.join("|",
                id, brand, model,
                String.valueOf(year),
                String.valueOf(rentalPricePerDay),
                String.valueOf(available),
                description.replace("|", ";"),
                type
        );
    }

    // Factory method — parses a file line back into the correct subclass
    public static Vehicle fromFileString(String line) {
        String[] parts = line.split("\\|", -1);
        if (parts.length < 8) return null;

        String id          = parts[0];
        String brand       = parts[1];
        String model       = parts[2];
        int year           = Integer.parseInt(parts[3]);
        double price       = Double.parseDouble(parts[4]);
        boolean available  = Boolean.parseBoolean(parts[5]);
        String description = parts[6].replace(";", "|");
        String type        = parts[7];

        return switch (type.toUpperCase()) {
            case "CAR"  -> new Car(id, brand, model, year, price, available, description);
            case "VAN"  -> new Van(id, brand, model, year, price, available, description);
            case "BIKE" -> new Bike(id, brand, model, year, price, available, description);
            default     -> null;
        };
    }

    // --- Getters & Setters ---
    public String getId()                          { return id; }
    public void   setId(String id)                 { this.id = id; }

    public String getBrand()                       { return brand; }
    public void   setBrand(String brand)           { this.brand = brand; }

    public String getModel()                       { return model; }
    public void   setModel(String model)           { this.model = model; }

    public int    getYear()                        { return year; }
    public void   setYear(int year)                { this.year = year; }

    public double getRentalPricePerDay()           { return rentalPricePerDay; }
    public void   setRentalPricePerDay(double p)   { this.rentalPricePerDay = p; }

    public boolean isAvailable()                   { return available; }
    public void    setAvailable(boolean available) { this.available = available; }

    public String getDescription()                 { return description; }
    public void   setDescription(String d)         { this.description = d; }

    public String getType()                        { return type; }
    public void   setType(String type)             { this.type = type; }
}
