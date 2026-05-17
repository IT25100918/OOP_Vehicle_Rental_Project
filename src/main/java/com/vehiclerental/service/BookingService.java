package com.vehiclerental.service;

import com.vehiclerental.algorithm.SelectionSort;
import com.vehiclerental.linkedlist.LinkedList;
import com.vehiclerental.model.Booking;
import com.vehiclerental.util.FileHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookingService {

    private static final String FILE = "bookings.txt";
    private final FileHandler fileHandler;
    private final VehicleService vehicleService;

    @Autowired
    public BookingService(FileHandler fileHandler, VehicleService vehicleService) {
        this.fileHandler = fileHandler;
        this.vehicleService = vehicleService;
    }

    private LinkedList<Booking> loadAll() {
        LinkedList<Booking> list = new LinkedList<>();
        for (String line : fileHandler.readLines(FILE)) {
            Booking b = Booking.fromCsv(line);
            if (b != null) list.addLast(b);
        }
        return list;
    }

    private void saveAll(LinkedList<Booking> list) {
        List<String> lines = new ArrayList<>();
        for (Booking b : list.toList()) lines.add(b.toCsv());
        fileHandler.writeLines(FILE, lines);
    }

    // ─── CRUD Operations ────────────────────────────────────────────────────

    /** CREATE: Make a new booking - Abstraction: availability check is abstracted */
    public boolean createBooking(Booking booking) {
        // Abstraction: Check availability before booking
        if (!isVehicleAvailable(booking.getVehicleId())) return false;

        String id = fileHandler.generateId("BKG", FILE);
        booking.setBookingId(id);
        booking.setStatus("confirmed");
        booking.setCreatedAt(LocalDate.now().toString());

        // Calculate total days and cost
        LocalDate start = LocalDate.parse(booking.getStartDate());
        LocalDate end = LocalDate.parse(booking.getEndDate());
        int days = (int) ChronoUnit.DAYS.between(start, end);
        if (days <= 0) days = 1;
        booking.setTotalDays(days);

        double pricePerDay = vehicleService.findById(booking.getVehicleId()).getRentPrice();
        booking.setTotalCost(pricePerDay * days);

        // Mark vehicle as rented
        vehicleService.updateAvailability(booking.getVehicleId(), "rented");

        LinkedList<Booking> list = loadAll();
        list.addLast(booking);
        saveAll(list);
        return true;
    }

    /** READ: Get all bookings sorted by date */
    public List<Booking> getAllBookings() {
        List<Booking> bookings = loadAll().toList();
        SelectionSort.sortBookingsByDate(bookings);
        return bookings;
    }

    /** READ: Get bookings for specific user */
    public List<Booking> getBookingsByUser(String userId) {
        List<Booking> result = new ArrayList<>();
        for (Booking b : loadAll().toList()) {
            if (b.getUserId().equals(userId)) result.add(b);
        }
        SelectionSort.sortBookingsByDate(result);
        return result;
    }

    /** READ: Find booking by ID */
    public Booking findById(String bookingId) {
        for (Booking b : loadAll().toList()) {
            if (b.getBookingId().equals(bookingId)) return b;
        }
        return null;
    }

    /** UPDATE: Modify booking status */
    public boolean updateStatus(String bookingId, String status) {
        LinkedList<Booking> list = loadAll();
        List<Booking> bookings = list.toList();
        for (int i = 0; i < bookings.size(); i++) {
            if (bookings.get(i).getBookingId().equals(bookingId)) {
                bookings.get(i).setStatus(status);
                list.set(i, bookings.get(i));
                saveAll(list);
                return true;
            }
        }
        return false;
    }

    /** DELETE: Cancel a booking */
    public boolean cancelBooking(String bookingId) {
        Booking booking = findById(bookingId);
        if (booking == null) return false;

        // Release the vehicle
        vehicleService.updateAvailability(booking.getVehicleId(), "available");
        return updateStatus(bookingId, "cancelled");
    }

    /** Abstraction: Check vehicle availability */
    private boolean isVehicleAvailable(String vehicleId) {
        var vehicle = vehicleService.findById(vehicleId);
        return vehicle != null && vehicle.isAvailable();
    }

    public long countActive() {
        return getAllBookings().stream().filter(b -> "confirmed".equals(b.getStatus())).count();
    }
}
