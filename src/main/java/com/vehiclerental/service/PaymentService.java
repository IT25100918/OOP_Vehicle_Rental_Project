package com.vehiclerental.service;

import com.vehiclerental.algorithm.SelectionSort;
import com.vehiclerental.linkedlist.LinkedList;
import com.vehiclerental.model.Booking;
import com.vehiclerental.model.Payment;
import com.vehiclerental.util.FileHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentService {

    private static final String FILE = "payments.txt";
    private final FileHandler fileHandler;

    @Autowired
    public PaymentService(FileHandler fileHandler) {
        this.fileHandler = fileHandler;
    }

    private LinkedList<Payment> loadAll() {
        LinkedList<Payment> list = new LinkedList<>();
        for (String line : fileHandler.readLines(FILE)) {
            Payment p = Payment.fromCsv(line);
            if (p != null) list.addLast(p);
        }
        return list;
    }

    private void saveAll(LinkedList<Payment> list) {
        List<String> lines = new ArrayList<>();
        for (Payment p : list.toList()) lines.add(p.toCsv());
        fileHandler.writeLines(FILE, lines);
    }

    // ─── CRUD Operations ────────────────────────────────────────────────────

    /** CREATE: Generate a payment record */
    public boolean createPayment(Payment payment) {
        payment.setPaymentId(fileHandler.generateId("PAY", FILE));
        payment.setPaymentDate(LocalDate.now().toString());
        payment.setStatus("paid");
        payment.setTotalAmount(payment.getAmount() + payment.getLateFee());

        LinkedList<Payment> list = loadAll();
        list.addLast(payment);
        saveAll(list);
        return true;
    }

    /** READ: All payments sorted by date */
    public List<Payment> getAllPayments(String sortBy) {
        List<Payment> payments = loadAll().toList();
        if ("amount".equals(sortBy)) {
            SelectionSort.sortPaymentsByAmount(payments);
        } else {
            SelectionSort.sortPaymentsByDate(payments);
        }
        return payments;
    }

    public List<Payment> getAllPayments() {
        return getAllPayments("date");
    }

    /** READ: Payments by user */
    public List<Payment> getPaymentsByUser(String userId) {
        List<Payment> result = new ArrayList<>();
        for (Payment p : loadAll().toList()) {
            if (p.getUserId().equals(userId)) result.add(p);
        }
        SelectionSort.sortPaymentsByDate(result);
        return result;
    }

    /** READ: Find payment by booking ID */
    public Payment findByBookingId(String bookingId) {
        for (Payment p : loadAll().toList()) {
            if (p.getBookingId().equals(bookingId)) return p;
        }
        return null;
    }

    /** UPDATE: Update payment status */
    public boolean updateStatus(String paymentId, String status) {
        LinkedList<Payment> list = loadAll();
        List<Payment> payments = list.toList();
        for (int i = 0; i < payments.size(); i++) {
            if (payments.get(i).getPaymentId().equals(paymentId)) {
                payments.get(i).setStatus(status);
                list.set(i, payments.get(i));
                saveAll(list);
                return true;
            }
        }
        return false;
    }

    /** DELETE: Void a payment */
    public boolean delete(String paymentId) {
        LinkedList<Payment> list = loadAll();
        List<Payment> payments = list.toList();
        for (int i = 0; i < payments.size(); i++) {
            if (payments.get(i).getPaymentId().equals(paymentId)) {
                list.deleteByIndex(i);
                saveAll(list);
                return true;
            }
        }
        return false;
    }

    /**
     * Auto-calculate late fee based on how many days past the booking end date.
     * Charges 20% of the daily rate per overdue day.
     */
    public double calculateLateFee(Booking booking) {
        try {
            LocalDate endDate = LocalDate.parse(booking.getEndDate());
            LocalDate today = LocalDate.now();
            if (today.isAfter(endDate)) {
                long overdueDays = java.time.temporal.ChronoUnit.DAYS.between(endDate, today);
                double dailyRate = booking.getTotalDays() > 0
                        ? booking.getTotalCost() / booking.getTotalDays() : 0;
                return Math.round(dailyRate * 0.20 * overdueDays * 100.0) / 100.0;
            }
        } catch (Exception ignored) {}
        return 0.0;
    }

    /** Mark any confirmed bookings whose end date has passed as overdue */
    public List<Booking> getOverdueBookings(List<Booking> bookings) {
        LocalDate today = LocalDate.now();
        List<Booking> overdue = new ArrayList<>();
        for (Booking b : bookings) {
            if ("confirmed".equals(b.getStatus())) {
                try {
                    if (LocalDate.parse(b.getEndDate()).isBefore(today)) {
                        overdue.add(b);
                    }
                } catch (Exception ignored) {}
            }
        }
        return overdue;
    }

    /** Calculate total revenue */
    public double getTotalRevenue() {
        return getAllPayments().stream()
                .filter(p -> "paid".equals(p.getStatus()))
                .mapToDouble(Payment::getTotalAmount)
                .sum();
    }
}
