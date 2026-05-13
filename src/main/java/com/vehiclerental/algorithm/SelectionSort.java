package com.vehiclerental.algorithm;

import com.vehiclerental.model.Vehicle;

import java.util.List;

/**
 * Selection Sort Algorithm
 * Applied to Vehicle data in the Vehicle Rental Service Platform
 *
 * Selection Sort repeatedly finds the minimum element from the unsorted
 * portion of the list and places it in the correct position.
 * Time Complexity: O(n²) | Space Complexity: O(1)
 */
public class SelectionSort {

    // ─── Sort Vehicles ──────────────────────────────────────────────────────

    /** Sort vehicles by rent price (ascending) - cheapest first */
    public static void sortVehiclesByPrice(List<Vehicle> vehicles) {
        int n = vehicles.size();
        for (int i = 0; i < n - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < n; j++) {
                if (vehicles.get(j).getRentPrice() < vehicles.get(minIdx).getRentPrice()) {
                    minIdx = j;
                }
            }
            Vehicle temp = vehicles.get(minIdx);
            vehicles.set(minIdx, vehicles.get(i));
            vehicles.set(i, temp);
        }
    }

    /** Sort vehicles by availability — available vehicles appear first */
    public static void sortVehiclesByAvailability(List<Vehicle> vehicles) {
        int n = vehicles.size();
        for (int i = 0; i < n - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < n; j++) {
                if (vehicles.get(j).getAvailability().compareToIgnoreCase(
                        vehicles.get(minIdx).getAvailability()) < 0) {
                    minIdx = j;
                }
            }
            Vehicle temp = vehicles.get(minIdx);
            vehicles.set(minIdx, vehicles.get(i));
            vehicles.set(i, temp);
        }
    }

    /** Sort vehicles by type alphabetically */
    public static void sortVehiclesByType(List<Vehicle> vehicles) {
        int n = vehicles.size();
        for (int i = 0; i < n - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < n; j++) {
                if (vehicles.get(j).getType().compareToIgnoreCase(
                        vehicles.get(minIdx).getType()) < 0) {
                    minIdx = j;
                }
            }
            Vehicle temp = vehicles.get(minIdx);
            vehicles.set(minIdx, vehicles.get(i));
            vehicles.set(i, temp);
        }
    }
}
