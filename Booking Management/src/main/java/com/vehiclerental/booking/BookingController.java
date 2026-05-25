package com.vehiclerental.booking;

import com.vehiclerental.customer.User;
import com.vehiclerental.payment.PaymentService;
import com.vehiclerental.customer.UserService;
import com.vehiclerental.vehicle.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

@Controller
public class BookingController {

    @Autowired private BookingService bookingService;
    @Autowired private PaymentService paymentService;
    @Autowired private VehicleService vehicleService;
    @Autowired private UserService userService;

    @GetMapping("/bookings/edit/{bookingId}")
    public String showEditForm(@PathVariable String bookingId, Model model, HttpSession session) {
        User loggedIn = (User) session.getAttribute("loggedInUser");
        if (loggedIn == null) return "redirect:/login";
        if (!"ADMIN".equals(loggedIn.getRole()) && !"SUPER_ADMIN".equals(loggedIn.getRole()))
            return "redirect:/bookings";
        Booking booking = bookingService.findById(bookingId);
        if (booking == null) return "redirect:/bookings";
        model.addAttribute("user", loggedIn);
        model.addAttribute("booking", booking);
        return "booking/edit-booking";
    }

    @PostMapping("/bookings/update")
    public String updateBooking(
            @RequestParam String bookingId,
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam(required = false) String userName,
            @RequestParam(required = false) String vehicleName,
            @RequestParam(required = false) String status,
            HttpSession session) {
        User loggedIn = (User) session.getAttribute("loggedInUser");
        if (loggedIn == null) return "redirect:/login";
        if (!"ADMIN".equals(loggedIn.getRole()) && !"SUPER_ADMIN".equals(loggedIn.getRole()))
            return "redirect:/bookings";
        bookingService.updateBooking(bookingId, startDate, endDate, userName, vehicleName, status);
        return "redirect:/bookings";
    }

    @PostMapping("/bookings/confirm/{bookingId}")
    public String confirmBooking(@PathVariable String bookingId, HttpSession session) {
        User loggedIn = (User) session.getAttribute("loggedInUser");
        if (loggedIn == null) return "redirect:/login";
        if (!"ADMIN".equals(loggedIn.getRole()) && !"SUPER_ADMIN".equals(loggedIn.getRole()))
            return "redirect:/bookings";
        bookingService.confirmBooking(bookingId);
        return "redirect:/bookings";
    }

    @GetMapping("/bookings")
    public String listBookings(Model model, HttpSession session) {
        User loggedIn = (User) session.getAttribute("loggedInUser");
        if (loggedIn == null) return "redirect:/login";
        model.addAttribute("user", loggedIn);
        java.util.List<Booking> bookings;
        if ("ADMIN".equals(loggedIn.getRole()) || "SUPER_ADMIN".equals(loggedIn.getRole())) {
            bookings = bookingService.getBookingsSortedByDate();
        } else {
            bookings = bookingService.getBookingsSortedByDate().stream()
                    .filter(b -> loggedIn.getUserId().equals(b.getUserId()))
                    .collect(java.util.stream.Collectors.toList());
        }
        model.addAttribute("bookings", bookings);
        try {
            java.util.Set<String> paidBookingIds = paymentService.getAllPayments().stream()
                    .map(p -> p.getBookingId())
                    .collect(java.util.stream.Collectors.toSet());
            model.addAttribute("paidBookingIds", paidBookingIds);
        } catch (Exception e) {
            model.addAttribute("paidBookingIds", new java.util.HashSet<>());
        }
        return "booking/index";
    }

    @GetMapping("/bookings/add")
    public String showBookingForm(Model model, HttpSession session) {
        User loggedIn = (User) session.getAttribute("loggedInUser");
        if (loggedIn == null) return "redirect:/login";
        model.addAttribute("user", loggedIn);
        model.addAttribute("vehicles", vehicleService.getAvailableVehicles());
        model.addAttribute("users", userService.getAllUsers());
        return "booking/add-booking";
    }

    @PostMapping("/bookings/add")
    public String createBooking(
            @RequestParam(required = false) String userId,
            @RequestParam String vehicleId,
            @RequestParam String startDate, @RequestParam String endDate,
            @RequestParam(required = false) String pickupLocation,
            Model model, HttpSession session) {
        Object loggedIn = session.getAttribute("loggedInUser");
        if (loggedIn == null) return "redirect:/login";

        // If userId not provided (regular user), use their own ID
        if (userId == null || userId.isEmpty()) {
            if (loggedIn instanceof User) {
                userId = ((User) loggedIn).getUserId();
            }
        }

        User user = userService.findById(userId);
        String userName = user != null ? user.getFullName() : "Unknown";
        String bookingId = bookingService.createBookingAndGetId(userId, userName, vehicleId, startDate, endDate);
        if (bookingId != null) return "redirect:/payments/add?bookingId=" + bookingId;
        model.addAttribute("user", loggedIn);
        model.addAttribute("error", "Booking failed! Check dates or vehicle availability.");
        model.addAttribute("vehicles", vehicleService.getAvailableVehicles());
        model.addAttribute("users", userService.getAllUsers());
        return "booking/add-booking";
    }

    @PostMapping("/bookings/cancel/{bookingId}")
    public String cancelBooking(@PathVariable String bookingId, HttpSession session) {
        User loggedIn = (User) session.getAttribute("loggedInUser");
        if (loggedIn == null) return "redirect:/login";
        Booking booking = bookingService.findById(bookingId);
        if (booking == null) return "redirect:/bookings";
        boolean isAdmin = "ADMIN".equals(loggedIn.getRole()) || "SUPER_ADMIN".equals(loggedIn.getRole());
        if (!isAdmin && !loggedIn.getUserId().equals(booking.getUserId())) return "redirect:/bookings";
        bookingService.cancelBooking(bookingId);
        return "redirect:/bookings";
    }

    @PostMapping("/bookings/complete/{bookingId}")
    public String completeBooking(@PathVariable String bookingId, HttpSession session) {
        User loggedIn = (User) session.getAttribute("loggedInUser");
        if (loggedIn == null) return "redirect:/login";
        if (!"ADMIN".equals(loggedIn.getRole()) && !"SUPER_ADMIN".equals(loggedIn.getRole()))
            return "redirect:/bookings";
        bookingService.completeBooking(bookingId);
        return "redirect:/bookings";
    }

    @PostMapping("/bookings/delete/{bookingId}")
    public String deleteBooking(@PathVariable String bookingId, HttpSession session) {
        User loggedIn = (User) session.getAttribute("loggedInUser");
        if (loggedIn == null) return "redirect:/login";
        if (!"ADMIN".equals(loggedIn.getRole()) && !"SUPER_ADMIN".equals(loggedIn.getRole()))
            return "redirect:/bookings";
        bookingService.deleteBooking(bookingId);
        return "redirect:/bookings";
    }
}
