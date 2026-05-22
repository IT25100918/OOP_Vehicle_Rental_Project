package com.vehiclerental.shared;

import com.vehiclerental.vehicle.Vehicle;
import java.util.List;

/**
 * Selection sort implementations for domain entities.
 */
public class SelectionSort {

    public static void sortVehiclesByPrice(List<Vehicle> vehicles) {
        int n = vehicles.size();
        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (vehicles.get(j).getRentPrice() < vehicles.get(minIndex).getRentPrice())
                    minIndex = j;
            }
            Vehicle temp = vehicles.get(minIndex);
            vehicles.set(minIndex, vehicles.get(i));
            vehicles.set(i, temp);
        }
    }

    public static void sortVehiclesByAvailability(List<Vehicle> vehicles) {
        int n = vehicles.size();
        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (availabilityRank(vehicles.get(j).getAvailability())
                        < availabilityRank(vehicles.get(minIndex).getAvailability()))
                    minIndex = j;
            }
            Vehicle temp = vehicles.get(minIndex);
            vehicles.set(minIndex, vehicles.get(i));
            vehicles.set(i, temp);
        }
    }

    private static int availabilityRank(String availability) {
        switch (availability) {
            case "Available":   return 0;
            case "Rented":      return 1;
            case "Maintenance": return 2;
            default:            return 3;
        }
    }
}
