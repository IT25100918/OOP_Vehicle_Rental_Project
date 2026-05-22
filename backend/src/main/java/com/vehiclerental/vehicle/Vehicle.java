package com.vehiclerental.vehicle;

import com.vehiclerental.shared.FieldCodec;

public class Vehicle {

    private String vehicleId;
    private String type;
    private String brand;
    private String model;
    private String plateNumber;
    private double rentPrice;
    private String availability;
    private String description;
    private String imagePath;

    public Vehicle() {}

    public Vehicle(String vehicleId, String type, String brand,
                   String model, String plateNumber, double rentPrice,
                   String availability, String description, String imagePath) {
        this.vehicleId    = vehicleId;
        this.type         = type;
        this.brand        = brand;
        this.model        = model;
        this.plateNumber  = plateNumber;
        this.rentPrice    = rentPrice;
        this.availability = availability;
        this.description  = description;
        this.imagePath    = imagePath;
    }

    public Vehicle(String vehicleId, String type, String brand,
                   String model, String plateNumber, double rentPrice,
                   String availability, String description) {
        this(vehicleId, type, brand, model, plateNumber, rentPrice, availability, description, "none");
    }

    public String getVehicleId()   { return vehicleId; }
    public void setVehicleId(String vehicleId) { this.vehicleId = vehicleId; }
    public String getType()        { return type; }
    public void setType(String type) { this.type = type; }
    public String getBrand()       { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    public String getModel()       { return model; }
    public void setModel(String model) { this.model = model; }
    public String getPlateNumber() { return plateNumber; }
    public void setPlateNumber(String plateNumber) { this.plateNumber = plateNumber; }
    public double getRentPrice()   { return rentPrice; }
    public void setRentPrice(double rentPrice) { this.rentPrice = rentPrice; }
    public String getAvailability() { return availability; }
    public void setAvailability(String availability) { this.availability = availability; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getImagePath()   { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    public String toFileString() {
        return FieldCodec.encode(vehicleId) + "," +
               FieldCodec.encode(type) + "," +
               FieldCodec.encode(brand) + "," +
               FieldCodec.encode(model) + "," +
               FieldCodec.encode(plateNumber) + "," +
               rentPrice + "," +
               FieldCodec.encode(availability) + "," +
               FieldCodec.encode(description) + "," +
               FieldCodec.encode(imagePath != null ? imagePath : "none");
    }

    public static Vehicle fromFileString(String line) {
        String[] p = line.split(",", 9);
        String img = p.length > 8 ? FieldCodec.decode(p[8]) : "none";
        return new Vehicle(
            FieldCodec.decode(p[0]), FieldCodec.decode(p[1]),
            FieldCodec.decode(p[2]), FieldCodec.decode(p[3]),
            FieldCodec.decode(p[4]), Double.parseDouble(p[5]),
            FieldCodec.decode(p[6]), FieldCodec.decode(p[7]), img
        );
    }
}
