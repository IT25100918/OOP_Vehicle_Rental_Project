package com.vehiclerental.customer;

import com.vehiclerental.admin.Admin;
import com.vehiclerental.admin.AdminService;
import com.vehiclerental.booking.Booking;
import com.vehiclerental.booking.BookingService;
import com.vehiclerental.payment.Payment;
import com.vehiclerental.payment.PaymentService;
import com.vehiclerental.review.Review;
import com.vehiclerental.review.ReviewService;
import com.vehiclerental.vehicle.Vehicle;
import com.vehiclerental.vehicle.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class UserController {

    @Autowired private UserService userService;
    @Autowired private AdminService adminService;
    @Autowired private VehicleService vehicleService;
    @Autowired private BookingService bookingService;
    @Autowired private PaymentService paymentService;
    @Autowired private ReviewService reviewService;

    // ─── Auth ────────────────────────────────────────────────────────────────

    @GetMapping("/")
    public String home() { return "redirect:/login"; }

    @GetMapping("/register")
    public String showRegisterPage() { return "auth/register"; }

    @PostMapping("/register")
    public String registerUser(
            @RequestParam String fullName, @RequestParam String email,
            @RequestParam String password, @RequestParam String phoneNumber,
            @RequestParam String licenceNumber, Model model) {
        User user = new User();
        user.setFullName(fullName); user.setEmail(email);
        user.setPassword(password); user.setPhoneNumber(phoneNumber);
        user.setLicenceNumber(licenceNumber); user.setRole("USER");
        if (userService.registerUser(user)) {
            model.addAttribute("message", "Registration successful! Please login.");
            return "auth/login";
        }
        model.addAttribute("error", "Email already exists!");
        return "auth/register";
    }

    @GetMapping("/login")
    public String showLoginPage() { return "auth/login"; }

    @PostMapping("/login")
    public String loginUser(@RequestParam String email, @RequestParam String password,
                            Model model, HttpSession session) {
        Admin admin = adminService.loginAdmin(email, password);
        if (admin != null) {
            // Store a User proxy so the rest of the app only deals with User objects in session
            User adminAsUser = new User();
            adminAsUser.setUserId(admin.getAdminId());
            adminAsUser.setFullName(admin.getFullName());
            adminAsUser.setEmail(admin.getEmail());
            adminAsUser.setRole(admin.getRole());
            session.setAttribute("loggedInUser", adminAsUser);
            return "redirect:/dashboard";
        }
        User user = userService.loginUser(email, password);
        if (user != null) {
            session.setAttribute("loggedInUser", user);
            return "redirect:/dashboard";
        }
        model.addAttribute("error", "Invalid email or password!");
        return "auth/login";
    }

    @GetMapping("/forgot-password")
    public String showForgotPasswordPage() { return "auth/forgot-password"; }

    @PostMapping("/forgot-password")
    public String handleForgotPassword(
            @RequestParam String email, @RequestParam String newPassword,
            @RequestParam String confirmPassword, Model model) {
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match!");
            return "auth/forgot-password";
        }
        if (userService.findByEmail(email) == null) {
            model.addAttribute("error", "No account found with that email address.");
            return "auth/forgot-password";
        }
        userService.resetPassword(email, newPassword);
        model.addAttribute("message", "Password reset successful! Please log in.");
        return "auth/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    // ─── Dashboard ───────────────────────────────────────────────────────────

    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";

        boolean isAdmin = "ADMIN".equals(user.getRole()) || "SUPER_ADMIN".equals(user.getRole());

        List<Vehicle> vehicles = vehicleService.getAllVehicles();
        List<Booking> allBookings = bookingService.getAllBookings();
        List<Payment> allPayments = paymentService.getAllPayments();
        List<Review>  reviews  = reviewService.getAllReviews();
        List<User>    users    = userService.getAllUsers();

        String uid = user.getUserId();

        List<Booking> userBookings = allBookings.stream()
                .filter(b -> uid.equals(b.getUserId())).collect(Collectors.toList());
        List<Payment> userPayments = allPayments.stream()
                .filter(p -> uid.equals(p.getUserId())).collect(Collectors.toList());

        List<Booking> dashboardBookings = isAdmin ? allBookings : userBookings;
        List<Payment> dashboardPayments = isAdmin ? allPayments : userPayments;

        long totalVehicles       = vehicles.size();
        long availableVehicles   = vehicles.stream().filter(v -> "Available".equals(v.getAvailability())).count();
        long rentedVehicles      = vehicles.stream().filter(v -> "Rented".equals(v.getAvailability())).count();
        long maintenanceVehicles = vehicles.stream().filter(v -> "Maintenance".equals(v.getAvailability())).count();

        long activeBookings = dashboardBookings.stream().filter(b -> "Active".equals(b.getStatus())).count();
        double totalRevenue = dashboardPayments.stream().mapToDouble(Payment::getAmount).sum();

        int availPct = totalVehicles > 0 ? (int) Math.round(availableVehicles * 100.0 / totalVehicles) : 0;
        int rentPct  = totalVehicles > 0 ? (int) Math.round(rentedVehicles * 100.0 / totalVehicles) : 0;
        int maintPct = totalVehicles > 0 ? (int) Math.round(maintenanceVehicles * 100.0 / totalVehicles) : 0;

        List<Booking> recentBookings = dashboardBookings.size() > 5
                ? dashboardBookings.subList(dashboardBookings.size() - 5, dashboardBookings.size())
                : dashboardBookings;
        List<Payment> recentPayments = dashboardPayments.size() > 5
                ? dashboardPayments.subList(dashboardPayments.size() - 5, dashboardPayments.size())
                : dashboardPayments;
        List<Review> recentReviews = reviews.size() > 3
                ? reviews.subList(reviews.size() - 3, reviews.size()) : reviews;

        model.addAttribute("user",               user);
        model.addAttribute("vehicles",           vehicles);
        model.addAttribute("users",              users);
        model.addAttribute("userBookings",       userBookings);
        model.addAttribute("userPayments",       userPayments);
        model.addAttribute("totalVehicles",      totalVehicles);
        model.addAttribute("availableVehicles",  availableVehicles);
        model.addAttribute("rentedVehicles",     rentedVehicles);
        model.addAttribute("maintenanceVehicles",maintenanceVehicles);
        model.addAttribute("activeBookings",     activeBookings);
        model.addAttribute("totalUsers",         users.size());
        model.addAttribute("totalRevenue",       totalRevenue);
        model.addAttribute("availPct",           availPct);
        model.addAttribute("rentPct",            rentPct);
        model.addAttribute("maintPct",           maintPct);
        model.addAttribute("recentBookings",     recentBookings);
        model.addAttribute("recentPayments",     recentPayments);
        model.addAttribute("recentReviews",      recentReviews);
        model.addAttribute("totalBookings",      isAdmin ? allBookings.size() : userBookings.size());
        model.addAttribute("totalPayments",      isAdmin ? allPayments.size() : userPayments.size());

        return "dashboard";
    }

    // ─── Users CRUD ──────────────────────────────────────────────────────────

    @GetMapping("/users")
    public String listUsers(Model model, HttpSession session) {
        User loggedIn = (User) session.getAttribute("loggedInUser");
        if (loggedIn == null) return "redirect:/login";
        if (!"ADMIN".equals(loggedIn.getRole()) && !"SUPER_ADMIN".equals(loggedIn.getRole())) {
            return "redirect:/users/edit/" + loggedIn.getUserId();
        }
        model.addAttribute("user", loggedIn);
        model.addAttribute("users", userService.getAllUsers());
        return "customer/index";
    }

    @GetMapping("/users/edit/{userId}")
    public String showEditPage(@PathVariable String userId, Model model, HttpSession session) {
        model.addAttribute("user", session.getAttribute("loggedInUser"));
        model.addAttribute("editUser", userService.findById(userId));
        return "customer/edit-users";
    }

    @PostMapping("/users/update")
    public String updateUser(
            @RequestParam String userId,
            @RequestParam String fullName,
            @RequestParam String email,
            @RequestParam String phoneNumber,
            @RequestParam String licenceNumber,
            @RequestParam(required = false) String currentPassword,
            @RequestParam(required = false) String newPassword,
            HttpSession session,
            Model model) {
        User user = userService.findById(userId);
        if (user != null) {
            user.setFullName(fullName);
            user.setEmail(email);
            user.setPhoneNumber(phoneNumber);
            user.setLicenceNumber(licenceNumber);

            // Only change password if the user supplied both fields
            if (newPassword != null && !newPassword.trim().isEmpty()) {
                boolean changed = userService.changePassword(userId, currentPassword, newPassword.trim());
                if (!changed) {
                    model.addAttribute("user", session.getAttribute("loggedInUser"));
                    model.addAttribute("editUser", user);
                    model.addAttribute("pwError", "Current password is incorrect.");
                    return "customer/edit-users";
                }
                // Reload user after password change so session has the hashed value
                user = userService.findById(userId);
            } else {
                userService.updateUser(user);
            }

            // Keep session in sync if the user edited their own profile
            User loggedIn = (User) session.getAttribute("loggedInUser");
            if (loggedIn != null && loggedIn.getUserId().equals(userId)) {
                session.setAttribute("loggedInUser", user);
            }
        }
        User loggedIn = (User) session.getAttribute("loggedInUser");
        boolean isAdmin = loggedIn != null &&
                ("ADMIN".equals(loggedIn.getRole()) || "SUPER_ADMIN".equals(loggedIn.getRole()));
        return isAdmin ? "redirect:/users" : "redirect:/dashboard";
    }

    // Changed to POST to prevent CSRF via GET
    @PostMapping("/users/delete/{userId}")
    public String deleteUser(@PathVariable String userId, HttpSession session) {
        User loggedIn = (User) session.getAttribute("loggedInUser");
        if (loggedIn == null) return "redirect:/login";
        boolean isAdmin = "ADMIN".equals(loggedIn.getRole()) || "SUPER_ADMIN".equals(loggedIn.getRole());
        boolean isSelf  = loggedIn.getUserId().equals(userId);
        if (!isAdmin && !isSelf) return "redirect:/users";
        userService.deleteUser(userId);
        if (isSelf && !isAdmin) {
            session.invalidate();
            return "redirect:/login";
        }
        return "redirect:/users";
    }

    // ─── REST API endpoints ────────────────────────────────────────────────

    private boolean isAdmin(HttpSession session) {
        User u = (User) session.getAttribute("loggedInUser");
        return u != null && ("ADMIN".equals(u.getRole()) || "SUPER_ADMIN".equals(u.getRole()));
    }

    @GetMapping("/api/admin/bookings")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> apiAdminBookings(HttpSession session) {
        if (!isAdmin(session)) return ResponseEntity.status(403).build();
        List<Map<String, Object>> result = bookingService.getAllBookings().stream().map(b -> {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("id", b.getBookingId()); m.put("customerName", b.getUserName());
            m.put("vehicleName", b.getVehicleName()); m.put("pickupDate", b.getStartDate());
            m.put("returnDate", b.getEndDate()); m.put("amount", b.getTotalPrice());
            m.put("status", b.getStatus().toLowerCase()); return m;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/api/user/bookings")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> apiUserBookings(HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return ResponseEntity.status(401).build();
        List<Map<String, Object>> result = bookingService.getAllBookings().stream()
                .filter(b -> b.getUserId().equals(user.getUserId())).map(b -> {
                    Map<String, Object> m = new LinkedHashMap<>();
                    m.put("id", b.getBookingId()); m.put("vehicleName", b.getVehicleName());
                    m.put("pickupDate", b.getStartDate()); m.put("returnDate", b.getEndDate());
                    m.put("amount", b.getTotalPrice()); m.put("status", b.getStatus().toLowerCase()); return m;
                }).collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/api/admin/payments")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> apiAdminPayments(HttpSession session) {
        if (!isAdmin(session)) return ResponseEntity.status(403).build();
        List<Map<String, Object>> result = paymentService.getAllPayments().stream().map(p -> {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("id", p.getPaymentId()); m.put("customerName", p.getUserName());
            m.put("bookingId", p.getBookingId()); m.put("vehicleName", p.getVehicleName());
            m.put("amount", p.getAmount()); m.put("method", p.getPaymentMethod());
            m.put("date", p.getPaymentDate()); m.put("status", p.getStatus().toLowerCase()); return m;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/api/user/payments")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> apiUserPayments(HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return ResponseEntity.status(401).build();
        List<Map<String, Object>> result = paymentService.getAllPayments().stream()
                .filter(p -> p.getUserId().equals(user.getUserId())).map(p -> {
                    Map<String, Object> m = new LinkedHashMap<>();
                    m.put("id", p.getPaymentId()); m.put("bookingId", p.getBookingId());
                    m.put("vehicleName", p.getVehicleName()); m.put("amount", p.getAmount());
                    m.put("method", p.getPaymentMethod()); m.put("date", p.getPaymentDate());
                    m.put("status", p.getStatus().toLowerCase()); return m;
                }).collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/api/admin/reviews")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> apiAdminReviews(HttpSession session) {
        if (!isAdmin(session)) return ResponseEntity.status(403).build();
        List<Map<String, Object>> result = reviewService.getAllReviews().stream().map(r -> {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("id", r.getReviewId()); m.put("customerName", r.getUserName());
            m.put("vehicleName", r.getVehicleName()); m.put("rating", r.getRating());
            m.put("comment", r.getComment()); m.put("date", r.getReviewDate());
            m.put("status", r.getStatus()); return m;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/api/user/reviews")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> apiUserReviews(HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return ResponseEntity.status(401).build();
        List<Map<String, Object>> result = reviewService.getAllReviews().stream()
                .filter(r -> r.getUserId().equals(user.getUserId())).map(r -> {
                    Map<String, Object> m = new LinkedHashMap<>();
                    m.put("id", r.getReviewId()); m.put("vehicleName", r.getVehicleName());
                    m.put("rating", r.getRating()); m.put("comment", r.getComment());
                    m.put("date", r.getReviewDate()); m.put("status", r.getStatus()); return m;
                }).collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/api/admin/reviews/stats")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> apiReviewStats(HttpSession session) {
        if (!isAdmin(session)) return ResponseEntity.status(403).build();
        List<Review> reviews = reviewService.getAllReviews();
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("total", reviews.size());
        m.put("avgRating", reviews.stream().mapToInt(Review::getRating).average().orElse(0));
        m.put("pending",  reviews.stream().filter(r -> "Pending".equals(r.getStatus())).count());
        m.put("approved", reviews.stream().filter(r -> "Approved".equals(r.getStatus())).count());
        return ResponseEntity.ok(m);
    }

    @GetMapping("/api/admin/reviews/charts")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> apiReviewCharts(HttpSession session) {
        if (!isAdmin(session)) return ResponseEntity.status(403).build();
        List<Review> reviews = reviewService.getAllReviews();
        long[] dist = new long[5];
        for (Review r : reviews) { int i = r.getRating() - 1; if (i >= 0 && i < 5) dist[i]++; }
        return ResponseEntity.ok(Map.of("ratingDistribution", dist));
    }

    @PostMapping("/api/admin/reviews/status/{reviewId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> apiUpdateReviewStatus(
            @PathVariable String reviewId, HttpSession session) {
        if (!isAdmin(session)) return ResponseEntity.status(403).build();
        reviewService.approveReview(reviewId);
        return ResponseEntity.ok(Map.of("success", true));
    }

    @GetMapping("/api/admin/vehicles")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> apiAdminVehicles(HttpSession session) {
        if (!isAdmin(session)) return ResponseEntity.status(403).build();
        return ResponseEntity.ok(vehiclesAsJson(vehicleService.getAllVehicles()));
    }

    @GetMapping("/api/vehicles")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> apiPublicVehicles() {
        return ResponseEntity.ok(vehiclesAsJson(vehicleService.getAvailableVehicles()));
    }

    private List<Map<String, Object>> vehiclesAsJson(List<Vehicle> vehicles) {
        return vehicles.stream().map(v -> {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("id", v.getVehicleId()); m.put("type", v.getType());
            m.put("brand", v.getBrand()); m.put("model", v.getModel());
            m.put("name", v.getBrand() + " " + v.getModel());
            m.put("plateNumber", v.getPlateNumber());
            m.put("rentPrice", v.getRentPrice()); m.put("price", v.getRentPrice());
            m.put("availability", v.getAvailability()); m.put("status", v.getAvailability());
            m.put("description", v.getDescription());
            m.put("imagePath", v.getImagePath()); m.put("image", v.getImagePath());
            return m;
        }).collect(Collectors.toList());
    }

    @GetMapping("/api/admin/users")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> apiAdminUsers(HttpSession session) {
        if (!isAdmin(session)) return ResponseEntity.status(403).build();
        List<Map<String, Object>> result = userService.getAllUsers().stream().map(u -> {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("id", u.getUserId()); m.put("fullName", u.getFullName());
            m.put("email", u.getEmail()); m.put("phone", u.getPhoneNumber());
            m.put("license", u.getLicenceNumber()); m.put("role", u.getRole()); return m;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/api/admin/dashboard/stats")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> apiDashboardStats(HttpSession session) {
        if (!isAdmin(session)) return ResponseEntity.status(403).build();
        List<Booking> bookings = bookingService.getAllBookings();
        List<Payment> payments = paymentService.getAllPayments();
        List<Vehicle> vehicles = vehicleService.getAllVehicles();
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("totalBookings",    bookings.size());
        m.put("totalVehicles",    vehicles.size());
        m.put("totalUsers",       userService.getAllUsers().size());
        m.put("totalRevenue",     payments.stream().mapToDouble(Payment::getAmount).sum());
        m.put("activeBookings",   bookings.stream().filter(b -> "Active".equals(b.getStatus())).count());
        m.put("availableVehicles",vehicles.stream().filter(v -> "Available".equals(v.getAvailability())).count());
        return ResponseEntity.ok(m);
    }

    @GetMapping("/api/admin/dashboard/recent-bookings")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> apiRecentBookings(HttpSession session) {
        if (!isAdmin(session)) return ResponseEntity.status(403).build();
        List<Booking> all = bookingService.getBookingsSortedByDate();
        List<Booking> recent = all.size() > 5 ? all.subList(all.size() - 5, all.size()) : all;
        List<Map<String, Object>> result = recent.stream().map(b -> {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("bookingId", b.getBookingId()); m.put("customerName", b.getUserName());
            m.put("vehicleName", b.getVehicleName()); m.put("date", b.getStartDate());
            m.put("amount", b.getTotalPrice()); m.put("status", b.getStatus().toLowerCase()); return m;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/api/admin/dashboard/recent-users")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> apiRecentUsers(HttpSession session) {
        if (!isAdmin(session)) return ResponseEntity.status(403).build();
        List<User> all = userService.getAllUsers();
        List<User> recent = all.size() > 5 ? all.subList(all.size() - 5, all.size()) : all;
        List<Map<String, Object>> result = recent.stream().map(u -> {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("userId", u.getUserId()); m.put("fullName", u.getFullName());
            m.put("email", u.getEmail()); m.put("status", "Active"); return m;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/api/admin/dashboard/charts")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> apiDashboardCharts(HttpSession session) {
        if (!isAdmin(session)) return ResponseEntity.status(403).build();
        List<Vehicle> vehicles = vehicleService.getAllVehicles();
        return ResponseEntity.ok(Map.of(
                "available",   vehicles.stream().filter(v -> "Available".equals(v.getAvailability())).count(),
                "rented",      vehicles.stream().filter(v -> "Rented".equals(v.getAvailability())).count(),
                "maintenance", vehicles.stream().filter(v -> "Maintenance".equals(v.getAvailability())).count()
        ));
    }
}
