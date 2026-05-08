package com.vehiclerental.service;

import com.vehiclerental.algorithm.SelectionSort;
import com.vehiclerental.linkedlist.LinkedList;
import com.vehiclerental.model.Vehicle;
import com.vehiclerental.util.FileHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VehicleService {

    private static final String FILE = "vehicles.txt";
    private final FileHandler fileHandler;

    @Autowired
    public VehicleService(FileHandler fileHandler) {
        this.fileHandler = fileHandler;
        seedSampleVehicles();
    }

    private LinkedList<Vehicle> loadAll() {
        LinkedList<Vehicle> list = new LinkedList<>();
        for (String line : fileHandler.readLines(FILE)) {
            Vehicle v = Vehicle.fromCsv(line);
            if (v != null) list.addLast(v);
        }
        return list;
    }

    private void saveAll(LinkedList<Vehicle> list) {
        List<String> lines = new ArrayList<>();
        for (Vehicle v : list.toList()) lines.add(v.toCsv());
        fileHandler.writeLines(FILE, lines);
    }

    // ─── CRUD Operations ────────────────────────────────────────────────────

    /** CREATE: Register a new vehicle */
    public boolean addVehicle(Vehicle vehicle) {
        LinkedList<Vehicle> list = loadAll();
        // Check duplicate plate number
        for (Vehicle v : list.toList()) {
            if (v.getPlateNumber().equalsIgnoreCase(vehicle.getPlateNumber())) return false;
        }
        vehicle.setVehicleId(fileHandler.generateId("VEH", FILE));
        list.addLast(vehicle);
        saveAll(list);
        return true;
    }

    /** READ: All vehicles sorted by availability then price */
    public List<Vehicle> getAllVehicles(String sortBy) {
        List<Vehicle> vehicles = loadAll().toList();
        switch (sortBy == null ? "availability" : sortBy) {
            case "price"        -> SelectionSort.sortVehiclesByPrice(vehicles);
            case "type"         -> SelectionSort.sortVehiclesByType(vehicles);
            default             -> SelectionSort.sortVehiclesByAvailability(vehicles);
        }
        return vehicles;
    }

    public List<Vehicle> getAllVehicles() {
        return getAllVehicles("availability");
    }

    /** READ: Only available vehicles */
    public List<Vehicle> getAvailableVehicles() {
        List<Vehicle> result = new ArrayList<>();
        for (Vehicle v : loadAll().toList()) {
            if (v.isAvailable()) result.add(v);
        }
        SelectionSort.sortVehiclesByPrice(result);
        return result;
    }

    /** READ: Filter by type */
    public List<Vehicle> getByType(String type) {
        List<Vehicle> result = new ArrayList<>();
        for (Vehicle v : loadAll().toList()) {
            if (v.getType().equalsIgnoreCase(type)) result.add(v);
        }
        SelectionSort.sortVehiclesByPrice(result);
        return result;
    }

    /** READ: Find vehicle by ID */
    public Vehicle findById(String vehicleId) {
        for (Vehicle v : loadAll().toList()) {
            if (v.getVehicleId().equals(vehicleId)) return v;
        }
        return null;
    }

    /** UPDATE: Edit vehicle details */
    public boolean update(Vehicle updated) {
        LinkedList<Vehicle> list = loadAll();
        List<Vehicle> vehicles = list.toList();
        for (int i = 0; i < vehicles.size(); i++) {
            if (vehicles.get(i).getVehicleId().equals(updated.getVehicleId())) {
                list.set(i, updated);
                saveAll(list);
                return true;
            }
        }
        return false;
    }

    /** UPDATE: Mark vehicle as rented or available */
    public boolean updateAvailability(String vehicleId, String status) {
        Vehicle v = findById(vehicleId);
        if (v == null) return false;
        v.setAvailability(status);
        return update(v);
    }

    /** DELETE: Remove vehicle */
    public boolean delete(String vehicleId) {
        LinkedList<Vehicle> list = loadAll();
        List<Vehicle> vehicles = list.toList();
        for (int i = 0; i < vehicles.size(); i++) {
            if (vehicles.get(i).getVehicleId().equals(vehicleId)) {
                list.deleteByIndex(i);
                saveAll(list);
                return true;
            }
        }
        return false;
    }

    /** Count available vehicles */
    public long countAvailable() {
        return getAvailableVehicles().size();
    }

    /** Seed sample vehicles on first run */
    private void seedSampleVehicles() {
        if (!fileHandler.fileExists(FILE) || fileHandler.readLines(FILE).isEmpty()) {
            List<Vehicle> samples = List.of(
                new Vehicle("VEH001","car","Toyota","Corolla","CAB-1234",4500.0,"available","https://images.unsplash.com/photo-1549399542-7e8ee8c8f9e8?w=400","6.9271,79.8612"),
                new Vehicle("VEH002","car","Honda","Civic","CAB-5678",5500.0,"available","https://images.unsplash.com/photo-1617469767-8f2195df7b66?w=400","7.2906,80.6337"),
                new Vehicle("VEH003","van","Toyota","HiAce","VAN-1111",8000.0,"available","https://images.unsplash.com/photo-1524638431109-93d95c968f03?w=400","6.0535,80.2210"),
                new Vehicle("VEH004","bike","Honda","CB150R","BIK-2222",1500.0,"available","https://images.unsplash.com/photo-1558618666-fcd25c85cd64?w=400","7.8731,80.6550"),
                new Vehicle("VEH005","car","BMW","3 Series","CAB-9900",12000.0,"available","https://images.unsplash.com/photo-1555215695-3004980ad54e?w=400","6.9344,81.0000"),
                new Vehicle("VEH006","van","Nissan","Caravan","VAN-3333",7500.0,"rented","https://images.unsplash.com/photo-1541899481282-d53bffe3c35d?w=400","8.3114,80.4037")
            );
            LinkedList<Vehicle> list = new LinkedList<>();
            for (Vehicle v : samples) list.addLast(v);
            saveAll(list);
        }
    }
}
