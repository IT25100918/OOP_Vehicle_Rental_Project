package com.vehiclerental.service;

import com.vehiclerental.model.Vehicle;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * VehicleService — all CRUD operations use file read/write (vehicles.txt).
 */
@Service
public class VehicleService {

    @Value("${vehicles.file.path}")
    private String filePath;

    // ─────────────────────────────────────────────
    // CREATE — Add a new vehicle to vehicles.txt
    // ─────────────────────────────────────────────
    public void addVehicle(Vehicle vehicle) throws IOException {
        ensureFileExists();
        vehicle.setId(generateId());
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(vehicle.toFileString());
            writer.newLine();
        }
    }

    // ─────────────────────────────────────────────
    // READ — Load all vehicles from file
    // ─────────────────────────────────────────────
    public List<Vehicle> getAllVehicles() throws IOException {
        ensureFileExists();
        List<Vehicle> vehicles = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isBlank()) {
                    Vehicle v = Vehicle.fromFileString(line.trim());
                    if (v != null) vehicles.add(v);
                }
            }
        }
        return vehicles;
    }

    // READ — Get a single vehicle by ID
    public Optional<Vehicle> getVehicleById(String id) throws IOException {
        return getAllVehicles().stream()
                .filter(v -> v.getId().equals(id))
                .findFirst();
    }

    // READ — Search vehicles by type, max price, and/or availability
    public List<Vehicle> searchVehicles(String type, Double maxPrice, Boolean available) throws IOException {
        return getAllVehicles().stream()
                .filter(v -> type == null || type.isBlank() || v.getType().equalsIgnoreCase(type))
                .filter(v -> maxPrice == null || v.getRentalPricePerDay() <= maxPrice)
                .filter(v -> available == null || v.isAvailable() == available)
                .collect(Collectors.toList());
    }

    // ─────────────────────────────────────────────
    // UPDATE — Edit a vehicle record in file
    // ─────────────────────────────────────────────
    public boolean updateVehicle(Vehicle updated) throws IOException {
        List<Vehicle> all = getAllVehicles();
        boolean found = false;
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getId().equals(updated.getId())) {
                // Preserve the type; update everything else
                updated.setType(updated.getType());
                all.set(i, updated);
                found = true;
                break;
            }
        }
        if (found) writeAll(all);
        return found;
    }

    // ─────────────────────────────────────────────
    // DELETE — Remove a vehicle from file
    // ─────────────────────────────────────────────
    public boolean deleteVehicle(String id) throws IOException {
        List<Vehicle> all = getAllVehicles();
        boolean removed = all.removeIf(v -> v.getId().equals(id));
        if (removed) writeAll(all);
        return removed;
    }

    // ─────────────────────────────────────────────
    // Helpers
    // ─────────────────────────────────────────────
    private void writeAll(List<Vehicle> vehicles) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false))) {
            for (Vehicle v : vehicles) {
                writer.write(v.toFileString());
                writer.newLine();
            }
        }
    }

    private void ensureFileExists() throws IOException {
        File file = new File(filePath);
        file.getParentFile().mkdirs();
        if (!file.exists()) file.createNewFile();
    }

    private String generateId() {
        return "VH-" + System.currentTimeMillis();
    }
}
