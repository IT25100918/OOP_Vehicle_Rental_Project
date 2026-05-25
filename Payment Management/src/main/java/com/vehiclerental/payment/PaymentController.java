package com.vehiclerental.payment;

import com.vehiclerental.booking.Booking;
import com.vehiclerental.booking.BookingService;
import com.vehiclerental.vehicle.Vehicle;
import com.vehiclerental.vehicle.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import com.vehiclerental.customer.User;

@Controller
public class PaymentController {

    @Autowired private PaymentService paymentService;
    @Autowired private BookingService bookingService;
    @Autowired private VehicleService vehicleService;

    @GetMapping("/payments/edit/{paymentId}")
    public String showEditPayment(@PathVariable String paymentId, Model model, HttpSession session) {
        User loggedIn = (User) session.getAttribute("loggedInUser");
        if (loggedIn == null) return "redirect:/login";
        if (!"ADMIN".equals(loggedIn.getRole()) && !"SUPER_ADMIN".equals(loggedIn.getRole()))
            return "redirect:/payments";
        model.addAttribute("user", loggedIn);
        model.addAttribute("payment", paymentService.findById(paymentId));
        return "payment/edit-payment";
    }

    @PostMapping("/payments/update")
    public String updatePayment(
            @RequestParam String paymentId,
            @RequestParam String paymentMethod,
            @RequestParam String status,
            HttpSession session) {
        User loggedIn = (User) session.getAttribute("loggedInUser");
        if (loggedIn == null) return "redirect:/login";
        if (!"ADMIN".equals(loggedIn.getRole()) && !"SUPER_ADMIN".equals(loggedIn.getRole()))
            return "redirect:/payments";
        paymentService.updatePayment(paymentId, paymentMethod, status);
        return "redirect:/payments";
    }

    @GetMapping("/payments")
    public String listPayments(Model model, HttpSession session) {
        User loggedIn = (User) session.getAttribute("loggedInUser");
        if (loggedIn == null) return "redirect:/login";
        model.addAttribute("user", loggedIn);
        if ("ADMIN".equals(loggedIn.getRole()) || "SUPER_ADMIN".equals(loggedIn.getRole())) {
            model.addAttribute("payments", paymentService.getAllPayments());
        } else {
            model.addAttribute("payments", paymentService.getAllPayments().stream()
                    .filter(p -> loggedIn.getUserId().equals(p.getUserId()))
                    .collect(java.util.stream.Collectors.toList()));
        }
        return "payment/index";
    }

    @GetMapping("/payments/add")
    public String showAddPayment(@RequestParam(required = false) String bookingId,
                                 Model model, HttpSession session) {
        User loggedIn = (User) session.getAttribute("loggedInUser");
        if (loggedIn == null) return "redirect:/login";
        model.addAttribute("user", loggedIn);
        model.addAttribute("bookings", bookingService.getAllBookings());
        if (bookingId != null) {
            model.addAttribute("selectedBookingId", bookingId);
            Booking b = bookingService.findById(bookingId);
            if (b != null) {
                model.addAttribute("selectedBooking", b);
                Vehicle v = vehicleService.findById(b.getVehicleId());
                if (v != null) model.addAttribute("selectedVehicle", v);
            }
        }
        return "payment/add-payment";
    }

    @PostMapping("/payments/add")
    public String addPayment(
            @RequestParam String bookingId, @RequestParam String paymentMethod,
            Model model, HttpSession session) {
        User loggedIn = (User) session.getAttribute("loggedInUser");
        if (loggedIn == null) return "redirect:/login";
        Booking booking = bookingService.findById(bookingId);
        if (booking == null) {
            model.addAttribute("user", loggedIn);
            model.addAttribute("error", "Booking not found!");
            model.addAttribute("bookings", bookingService.getAllBookings());
            return "payment/add-payment";
        }
        // Regular users can only pay for their own bookings
        boolean isAdmin = "ADMIN".equals(loggedIn.getRole()) || "SUPER_ADMIN".equals(loggedIn.getRole());
        if (!isAdmin && !loggedIn.getUserId().equals(booking.getUserId()))
            return "redirect:/payments";
        if (paymentService.paymentExistsForBooking(bookingId)) {
            model.addAttribute("user", loggedIn);
            model.addAttribute("error", "A payment already exists for this booking!");
            model.addAttribute("bookings", bookingService.getAllBookings());
            return "payment/add-payment";
        }
        boolean success = paymentService.addPayment(bookingId, booking.getUserId(),
                booking.getUserName(), booking.getVehicleName(),
                booking.getTotalPrice(), paymentMethod);
        if (success) return "redirect:/payments";
        model.addAttribute("user", loggedIn);
        model.addAttribute("error", "Payment failed!");
        model.addAttribute("bookings", bookingService.getAllBookings());
        return "payment/add-payment";
    }

    // FIXED: added auth + role-scoped data
    @GetMapping("/payments/sort/amount")
    public String sortByAmount(Model model, HttpSession session) {
        User loggedIn = (User) session.getAttribute("loggedInUser");
        if (loggedIn == null) return "redirect:/login";
        model.addAttribute("user", loggedIn);
        if ("ADMIN".equals(loggedIn.getRole()) || "SUPER_ADMIN".equals(loggedIn.getRole())) {
            model.addAttribute("payments", paymentService.getPaymentsSortedByAmount());
        } else {
            model.addAttribute("payments", paymentService.getPaymentsSortedByAmount().stream()
                    .filter(p -> loggedIn.getUserId().equals(p.getUserId()))
                    .collect(java.util.stream.Collectors.toList()));
        }
        return "payment/index";
    }

    @PostMapping("/payments/overdue/{paymentId}")
    public String markOverdue(@PathVariable String paymentId, HttpSession session) {
        User loggedIn = (User) session.getAttribute("loggedInUser");
        if (loggedIn == null) return "redirect:/login";
        if (!"ADMIN".equals(loggedIn.getRole()) && !"SUPER_ADMIN".equals(loggedIn.getRole()))
            return "redirect:/payments";
        paymentService.updateStatus(paymentId, "Overdue");
        return "redirect:/payments";
    }

    @PostMapping("/payments/delete/{paymentId}")
    public String deletePayment(@PathVariable String paymentId, HttpSession session) {
        User loggedIn = (User) session.getAttribute("loggedInUser");
        if (loggedIn == null) return "redirect:/login";
        if (!"ADMIN".equals(loggedIn.getRole()) && !"SUPER_ADMIN".equals(loggedIn.getRole()))
            return "redirect:/payments";
        paymentService.deletePayment(paymentId);
        return "redirect:/payments";
    }
}
