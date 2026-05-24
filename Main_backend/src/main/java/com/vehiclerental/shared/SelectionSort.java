package com.vehiclerental.shared;

import com.vehiclerental.vehicle.Vehicle;
import java.util.Comparator;
import java.util.List;

public class SelectionSort {

    public static <T> void sort(List<T> list, Comparator<T> comparator) {
        int n = list.size();
        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (comparator.compare(list.get(j), list.get(minIndex)) < 0)
                    minIndex = j;
            }
            T temp = list.get(minIndex);
            list.set(minIndex, list.get(i));
            list.set(i, temp);
        }
    }

    public static void sortVehiclesByPrice(List<Vehicle> vehicles) {
        sort(vehicles, Comparator.comparingDouble(Vehicle::getRentPrice));
    }

    public static void sortVehiclesByAvailability(List<Vehicle> vehicles) {
        sort(vehicles, Comparator.comparing(Vehicle::getAvailability));
    }
}
