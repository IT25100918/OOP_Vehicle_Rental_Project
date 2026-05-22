package com.vehiclerental.review;

import com.vehiclerental.customer.User;
import com.vehiclerental.vehicle.Vehicle;
import com.vehiclerental.vehicle.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

@Controller
public class ReviewController {

    @Autowired private ReviewService reviewService;
    @Autowired private VehicleService vehicleService;

    @GetMapping("/reviews")
    public String listReviews(Model model, HttpSession session) {
        User loggedIn = (User) session.getAttribute("loggedInUser");
        if (loggedIn == null) return "redirect:/login";
        model.addAttribute("user", loggedIn);
        if ("ADMIN".equals(loggedIn.getRole()) || "SUPER_ADMIN".equals(loggedIn.getRole())) {
            model.addAttribute("reviews", reviewService.getAllReviews());
        } else {
            model.addAttribute("reviews", reviewService.getAllReviews().stream()
                    .filter(r -> loggedIn.getUserId().equals(r.getUserId()))
                    .collect(java.util.stream.Collectors.toList()));
        }
        return "review/index";
    }

    @GetMapping("/reviews/add")
    public String showAddForm(Model model, HttpSession session) {
        User loggedIn = (User) session.getAttribute("loggedInUser");
        if (loggedIn == null) return "redirect:/login";
        model.addAttribute("user", loggedIn);
        model.addAttribute("vehicles", vehicleService.getAllVehicles());
        return "review/add-review";
    }

    // FIX: read userId from session instead of trusting the form parameter
    @PostMapping("/reviews/add")
    public String addReview(
            @RequestParam String vehicleId,
            @RequestParam int rating,
            @RequestParam String comment,
            HttpSession session) {
        User loggedIn = (User) session.getAttribute("loggedInUser");
        if (loggedIn == null) return "redirect:/login";
        String userId       = loggedIn.getUserId();
        String userName     = loggedIn.getFullName();
        Vehicle foundVehicle = vehicleService.findById(vehicleId);
        String vehicleName  = foundVehicle != null ? foundVehicle.getBrand() + " " + foundVehicle.getModel() : "Unknown";
        reviewService.addReview(userId, userName, vehicleId, vehicleName, rating, comment);
        return "redirect:/reviews";
    }

    @GetMapping("/reviews/edit/{reviewId}")
    public String showEditForm(@PathVariable String reviewId, Model model, HttpSession session) {
        if (session.getAttribute("loggedInUser") == null) return "redirect:/login";
        model.addAttribute("user", session.getAttribute("loggedInUser"));
        model.addAttribute("review", reviewService.findById(reviewId));
        return "review/edit-review";
    }

    @PostMapping("/reviews/update")
    public String updateReview(
            @RequestParam String reviewId,
            @RequestParam String comment,
            @RequestParam int rating,
            HttpSession session) {
        if (session.getAttribute("loggedInUser") == null) return "redirect:/login";
        reviewService.updateReview(reviewId, comment, rating);
        return "redirect:/reviews";
    }

    @PostMapping("/reviews/approve/{reviewId}")
    public String approveReview(@PathVariable String reviewId, HttpSession session) {
        User loggedIn = (User) session.getAttribute("loggedInUser");
        if (loggedIn == null) return "redirect:/login";
        if (!"ADMIN".equals(loggedIn.getRole()) && !"SUPER_ADMIN".equals(loggedIn.getRole()))
            return "redirect:/reviews";
        reviewService.approveReview(reviewId);
        return "redirect:/reviews";
    }

    @GetMapping("/reviews/sort/rating")
    public String sortByRating(Model model, HttpSession session) {
        User loggedIn = (User) session.getAttribute("loggedInUser");
        if (loggedIn == null) return "redirect:/login";
        model.addAttribute("user", loggedIn);
        model.addAttribute("reviews", reviewService.getReviewsSortedByRating());
        return "review/index";
    }

    @PostMapping("/reviews/delete/{reviewId}")
    public String deleteReview(@PathVariable String reviewId, HttpSession session) {
        if (session.getAttribute("loggedInUser") == null) return "redirect:/login";
        reviewService.deleteReview(reviewId);
        return "redirect:/reviews";
    }
}