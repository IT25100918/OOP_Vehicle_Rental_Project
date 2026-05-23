package com.vehiclerental.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    @Autowired private PaymentRepository paymentRepository;

    public boolean paymentExistsForBooking(String bookingId) {
        return getAllPayments().stream().anyMatch(p -> p.getBookingId().equals(bookingId));
    }

    public boolean addPayment(String bookingId, String userId, String userName,
                               String vehicleName, double amount, String paymentMethod) {
        Payment payment = new Payment("P" + java.util.UUID.randomUUID().toString().replace("-","").substring(0,12), bookingId, userId,
                userName, vehicleName, amount, LocalDate.now().toString(), paymentMethod, "Paid");
        return paymentRepository.append(payment);
    }

    public List<Payment> getAllPayments() { return paymentRepository.readAll(); }

    public Payment findById(String paymentId) {
        return getAllPayments().stream()
                .filter(p -> p.getPaymentId().equals(paymentId))
                .findFirst().orElse(null);
    }

    public List<Payment> getPaymentsSortedByAmount() {
        List<Payment> payments = getAllPayments();
        int n = payments.size();
        for (int i = 0; i < n - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < n; j++)
                if (payments.get(j).getAmount() < payments.get(minIdx).getAmount()) minIdx = j;
            Payment tmp = payments.get(minIdx);
            payments.set(minIdx, payments.get(i));
            payments.set(i, tmp);
        }
        return payments;
    }

    public boolean updatePayment(String paymentId, String paymentMethod, String status) {
        List<Payment> payments = getAllPayments();
        boolean found = false;
        for (Payment p : payments)
            if (p.getPaymentId().equals(paymentId)) {
                p.setPaymentMethod(paymentMethod);
                p.setStatus(status);
                found = true; break;
            }
        if (found) paymentRepository.saveAll(payments);
        return found;
    }

    public boolean updateStatus(String paymentId, String status) {
        List<Payment> payments = getAllPayments();
        boolean found = false;
        for (Payment p : payments)
            if (p.getPaymentId().equals(paymentId)) { p.setStatus(status); found = true; break; }
        if (found) paymentRepository.saveAll(payments);
        return found;
    }

    public boolean deletePayment(String paymentId) {
        List<Payment> payments = getAllPayments();
        boolean removed = payments.removeIf(p -> p.getPaymentId().equals(paymentId));
        if (removed) paymentRepository.saveAll(payments);
        return removed;
    }
}
