package com.vehiclerental.vehicle;

import com.vehiclerental.shared.SelectionSort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    public boolean addVehicle(Vehicle vehicle) {
        vehicle.setVehicleId("V" + java.util.UUID.randomUUID().toString().replace("-","").substring(0,12));
        return vehicleRepository.append(vehicle);
    }

    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.readAll();
    }

    public Vehicle findById(String vehicleId) {
        return getAllVehicles().stream()
                .filter(v -> v.getVehicleId().equals(vehicleId))
                .findFirst().orElse(null);
    }

    public List<Vehicle> getAvailableVehicles() {
        List<Vehicle> available = new ArrayList<>();
        for (Vehicle v : getAllVehicles())
            if ("Available".equals(v.getAvailability())) available.add(v);
        return available;
    }

    public boolean updateVehicle(Vehicle updated) {
        List<Vehicle> vehicles = getAllVehicles();
        boolean found = false;
        for (int i = 0; i < vehicles.size(); i++) {
            if (vehicles.get(i).getVehicleId().equals(updated.getVehicleId())) {
                vehicles.set(i, updated);
                found = true;
                break;
            }
        }
        if (found) vehicleRepository.saveAll(vehicles);
        return found;
    }

    public boolean deleteVehicle(String vehicleId) {
        List<Vehicle> vehicles = getAllVehicles();
        boolean removed = vehicles.removeIf(v -> v.getVehicleId().equals(vehicleId));
        if (removed) vehicleRepository.saveAll(vehicles);
        return removed;
    }

    public List<Vehicle> getVehiclesSortedByPrice() {
        List<Vehicle> vehicles = getAllVehicles();
        SelectionSort.sortVehiclesByPrice(vehicles);
        return vehicles;
    }

    public List<Vehicle> getVehiclesSortedByAvailability() {
        List<Vehicle> vehicles = getAllVehicles();
        SelectionSort.sortVehiclesByAvailability(vehicles);
        return vehicles;
    }
}
