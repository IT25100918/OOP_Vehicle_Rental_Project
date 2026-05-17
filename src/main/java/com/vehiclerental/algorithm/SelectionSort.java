package com.vehiclerental.algorithm;

import com.vehiclerental.model.Vehicle;
import com.vehiclerental.model.Booking;
import com.vehiclerental.model.Payment;
import com.vehiclerental.model.Feedback;
import com.vehiclerental.model.User;

import java.util.List;

/**
 * Selection Sort Algorithm
 * Applied across multiple components of the Vehicle Rental Service Platform
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
            // Swap
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
                // "available" < "rented" alphabetically, so available comes first
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

    // ─── Sort Bookings ──────────────────────────────────────────────────────

    /** Sort bookings by start date (ascending) */
    public static void sortBookingsByDate(List<Booking> bookings) {
        int n = bookings.size();
        for (int i = 0; i < n - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < n; j++) {
                if (bookings.get(j).getStartDate().compareToIgnoreCase(
                        bookings.get(minIdx).getStartDate()) < 0) {
                    minIdx = j;
                }
            }
            Booking temp = bookings.get(minIdx);
            bookings.set(minIdx, bookings.get(i));
            bookings.set(i, temp);
        }
    }

    /** Sort bookings by status */
    public static void sortBookingsByStatus(List<Booking> bookings) {
        int n = bookings.size();
        for (int i = 0; i < n - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < n; j++) {
                if (bookings.get(j).getStatus().compareToIgnoreCase(
                        bookings.get(minIdx).getStatus()) < 0) {
                    minIdx = j;
                }
            }
            Booking temp = bookings.get(minIdx);
            bookings.set(minIdx, bookings.get(i));
            bookings.set(i, temp);
        }
    }

    // ─── Sort Payments ──────────────────────────────────────────────────────

    /** Sort payments by total amount (ascending) */
    public static void sortPaymentsByAmount(List<Payment> payments) {
        int n = payments.size();
        for (int i = 0; i < n - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < n; j++) {
                if (payments.get(j).getTotalAmount() < payments.get(minIdx).getTotalAmount()) {
                    minIdx = j;
                }
            }
            Payment temp = payments.get(minIdx);
            payments.set(minIdx, payments.get(i));
            payments.set(i, temp);
        }
    }

    /** Sort payments by date */
    public static void sortPaymentsByDate(List<Payment> payments) {
        int n = payments.size();
        for (int i = 0; i < n - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < n; j++) {
                if (payments.get(j).getPaymentDate().compareToIgnoreCase(
                        payments.get(minIdx).getPaymentDate()) < 0) {
                    minIdx = j;
                }
            }
            Payment temp = payments.get(minIdx);
            payments.set(minIdx, payments.get(i));
            payments.set(i, temp);
        }
    }

    // ─── Sort Users ─────────────────────────────────────────────────────────

    /** Sort users alphabetically by name */
    public static void sortUsersByName(List<User> users) {
        int n = users.size();
        for (int i = 0; i < n - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < n; j++) {
                if (users.get(j).getName().compareToIgnoreCase(
                        users.get(minIdx).getName()) < 0) {
                    minIdx = j;
                }
            }
            User temp = users.get(minIdx);
            users.set(minIdx, users.get(i));
            users.set(i, temp);
        }
    }

    // ─── Sort Feedback ──────────────────────────────────────────────────────

    /** Sort feedback by rating descending (highest first) */
    public static void sortFeedbackByRating(List<Feedback> feedbacks) {
        int n = feedbacks.size();
        for (int i = 0; i < n - 1; i++) {
            int maxIdx = i;
            for (int j = i + 1; j < n; j++) {
                if (feedbacks.get(j).getRating() > feedbacks.get(maxIdx).getRating()) {
                    maxIdx = j;
                }
            }
            Feedback temp = feedbacks.get(maxIdx);
            feedbacks.set(maxIdx, feedbacks.get(i));
            feedbacks.set(i, temp);
        }
    }
}
