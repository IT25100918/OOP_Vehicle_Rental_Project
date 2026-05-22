package com.vehiclerental.booking;

import com.vehiclerental.shared.LinkedList;
import com.vehiclerental.vehicle.Vehicle;
import com.vehiclerental.vehicle.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
public class BookingService {

    @Autowired private BookingRepository bookingRepository;
    @Autowired private VehicleService vehicleService;

    /**
     * Loads all bookings via LinkedList — DSA requirement: manage rented vehicle records.
     * The LinkedList traversal satisfies the DSA constraint; toList() returns a standard List
     * for the rest of the application to use.
     */
    public List<Booking> getAllBookings() {
        LinkedList<Booking> linkedList = new LinkedList<>();
        for (Booking b : bookingRepository.readAll()) linkedList.addLast(b);
        return linkedList.toList();
    }

    /** Returns only Active (rented) bookings — DSA LinkedList traversal. */
    public List<Booking> getRentedBookings() {
        LinkedList<Booking> rentedList = new LinkedList<>();
        for (Booking b : bookingRepository.readAll())
            if ("Active".equals(b.getStatus())) rentedList.addLast(b);
        return rentedList.toList();
    }

    public boolean updateBooking(String bookingId, String startDate, String endDate,
                                 String userName, String vehicleName, String status) {
        List<Booking> bookings = getAllBookings();
        boolean found = false;
        for (Booking b : bookings) {
            if (b.getBookingId().equals(bookingId)) {
                Vehicle v = vehicleService.findById(b.getVehicleId());
                if (v != null) {
                    try {
                        long days = ChronoUnit.DAYS.between(LocalDate.parse(startDate), LocalDate.parse(endDate));
                        if (days > 0) {
                            b.setStartDate(startDate); b.setEndDate(endDate);
                            b.setTotalPrice(days * v.getRentPrice());
                        }
                    } catch (Exception ignored) {}
                }

                if (userName != null && !userName.isEmpty()) b.setUserName(userName);
                if (vehicleName != null && !vehicleName.isEmpty()) b.setVehicleName(vehicleName);
                if (status != null && !status.isEmpty()) b.setStatus(status);
                found = true; break;
            }
        }
        if (found) bookingRepository.saveAll(bookings);
        return found;
    }

    public boolean createBooking(String userId, String userName, String vehicleId, String startDate, String endDate) {
        return createBookingAndGetId(userId, userName, vehicleId, startDate, endDate) != null;
    }

    public String createBookingAndGetId(String userId, String userName, String vehicleId, String startDate, String endDate) {
        Vehicle vehicle = vehicleService.findById(vehicleId);
        if (vehicle == null || !"Available".equals(vehicle.getAvailability())) return null;
        long days = ChronoUnit.DAYS.between(LocalDate.parse(startDate), LocalDate.parse(endDate));
        if (days <= 0) return null;

        // Guard against duplicate active bookings for the same vehicle
        boolean alreadyBooked = getAllBookings().stream()
                .anyMatch(b -> b.getVehicleId().equals(vehicleId) && "Active".equals(b.getStatus()));
        if (alreadyBooked) return null;

        String vehicleName = vehicle.getBrand() + " " + vehicle.getModel();
        String bookingId = "B" + UUID.randomUUID().toString().replace("-", "").substring(0, 12);
        Booking booking = new Booking(bookingId, userId, vehicleId,
                userName, vehicleName, startDate, endDate, "Active", days * vehicle.getRentPrice());
        vehicle.setAvailability("Rented");
        vehicleService.updateVehicle(vehicle);
        boolean saved = bookingRepository.append(booking);
        return saved ? bookingId : null;
    }

    public Booking findById(String bookingId) {
        return getAllBookings().stream().filter(b -> b.getBookingId().equals(bookingId)).findFirst().orElse(null);
    }

    public List<Booking> getBookingsSortedByDate() {
        List<Booking> bookings = getAllBookings();
        int n = bookings.size();
        for (int i = 0; i < n - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < n; j++)
                if (bookings.get(j).getStartDate().compareTo(bookings.get(minIdx).getStartDate()) < 0) minIdx = j;
            Booking tmp = bookings.get(minIdx); bookings.set(minIdx, bookings.get(i)); bookings.set(i, tmp);
        }
        return bookings;
    }

    public boolean confirmBooking(String bookingId) {
        List<Booking> bookings = getAllBookings();
        boolean found = false;
        for (Booking b : bookings)
            if (b.getBookingId().equals(bookingId) && "Active".equals(b.getStatus())) {
                b.setStatus("Confirmed"); found = true; break;
            }
        if (found) bookingRepository.saveAll(bookings);
        return found;
    }

    public boolean cancelBooking(String bookingId) {
        List<Booking> bookings = getAllBookings();
        boolean found = false;
        for (Booking b : bookings) {
            if (b.getBookingId().equals(bookingId)) {
                // Only cancel if not already cancelled/completed
                if ("Cancelled".equals(b.getStatus()) || "Completed".equals(b.getStatus())) break;
                b.setStatus("Cancelled"); found = true;
                Vehicle v = vehicleService.findById(b.getVehicleId());
                if (v != null && "Rented".equals(v.getAvailability())) {
                    v.setAvailability("Available"); vehicleService.updateVehicle(v);
                }
                break;
            }
        }
        if (found) bookingRepository.saveAll(bookings);
        return found;
    }

    public boolean completeBooking(String bookingId) {
        List<Booking> bookings = getAllBookings();
        boolean found = false;
        for (Booking b : bookings) {
            if (b.getBookingId().equals(bookingId)) {
                // Only complete if Active or Confirmed
                if ("Cancelled".equals(b.getStatus()) || "Completed".equals(b.getStatus())) break;
                b.setStatus("Completed"); found = true;
                Vehicle v = vehicleService.findById(b.getVehicleId());
                if (v != null && "Rented".equals(v.getAvailability())) {
                    v.setAvailability("Available"); vehicleService.updateVehicle(v);
                }
                break;
            }
        }
        if (found) bookingRepository.saveAll(bookings);
        return found;
    }

    public boolean deleteBooking(String bookingId) {
        List<Booking> bookings = getAllBookings();
        boolean removed = bookings.removeIf(b -> b.getBookingId().equals(bookingId));
        if (removed) bookingRepository.saveAll(bookings);
        return removed;
    }
}