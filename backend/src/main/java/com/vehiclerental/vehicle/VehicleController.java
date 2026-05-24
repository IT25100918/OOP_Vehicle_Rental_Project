package com.vehiclerental.vehicle;

import com.vehiclerental.customer.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @GetMapping("/vehicles")
    public String listVehicles(Model model, HttpSession session) {
        model.addAttribute("user", session.getAttribute("loggedInUser"));
        model.addAttribute("vehicles", vehicleService.getAllVehicles());
        return "vehicle/index";
    }

    @GetMapping("/vehicles/add")
    public String showAddForm(HttpSession session, Model model) {
        User loggedIn = (User) session.getAttribute("loggedInUser");
        if (loggedIn == null) return "redirect:/login";
        if (!"ADMIN".equals(loggedIn.getRole()) && !"SUPER_ADMIN".equals(loggedIn.getRole()))
            return "redirect:/vehicles";
        model.addAttribute("user", loggedIn);
        return "vehicle/add-vehicle";
    }

    @PostMapping("/vehicles/add")
    public String addVehicle(
            @RequestParam String type, @RequestParam String brand,
            @RequestParam String model, @RequestParam String plateNumber,
            @RequestParam double rentPrice,
            @RequestParam(required = false, defaultValue = "") String description,
            @RequestParam(required = false) MultipartFile image,
            @RequestParam(required = false) String imagePath,
            Model modelAttr, HttpSession session) {

        Vehicle vehicle = new Vehicle();
        vehicle.setType(type); vehicle.setBrand(brand); vehicle.setModel(model);
        vehicle.setPlateNumber(plateNumber); vehicle.setRentPrice(rentPrice);
        vehicle.setAvailability("Available"); // new vehicles always start as Available
        vehicle.setDescription(description);

        if (image != null && !image.isEmpty()) {
            try {
                String uploadDir = "src/main/resources/static/images/vehicles/";
                Files.createDirectories(Paths.get(uploadDir));
                String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
                Files.write(Paths.get(uploadDir + fileName), image.getBytes());
                vehicle.setImagePath("/images/vehicles/" + fileName);
            } catch (IOException e) {
                vehicle.setImagePath("none");
            }
        } else if (imagePath != null && !imagePath.isEmpty()) {
            vehicle.setImagePath(imagePath);
        } else {
            vehicle.setImagePath("none");
        }

        vehicleService.addVehicle(vehicle);
        return "redirect:/vehicles";
    }

    @GetMapping("/vehicles/edit/{vehicleId}")
    public String showEditForm(@PathVariable String vehicleId, Model model, HttpSession session) {
        User loggedIn = (User) session.getAttribute("loggedInUser");
        if (loggedIn == null) return "redirect:/login";
        if (!"ADMIN".equals(loggedIn.getRole()) && !"SUPER_ADMIN".equals(loggedIn.getRole()))
            return "redirect:/vehicles";
        model.addAttribute("user", loggedIn);
        model.addAttribute("vehicle", vehicleService.findById(vehicleId));
        return "vehicle/edit-vehicle";
    }

    @PostMapping("/vehicles/update")
    public String updateVehicle(
            @RequestParam String vehicleId, @RequestParam String type,
            @RequestParam String brand, @RequestParam String model,
            @RequestParam String plateNumber, @RequestParam double rentPrice,
            @RequestParam String availability,
            @RequestParam(required = false, defaultValue = "") String description,
            @RequestParam(required = false) MultipartFile image,
            @RequestParam(required = false) String imagePath) {

        Vehicle existing = vehicleService.findById(vehicleId);
        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleId(vehicleId); vehicle.setType(type);
        vehicle.setBrand(brand); vehicle.setModel(model);
        vehicle.setPlateNumber(plateNumber); vehicle.setRentPrice(rentPrice);
        vehicle.setAvailability(availability); vehicle.setDescription(description);

        if (image != null && !image.isEmpty()) {
            try {
                String uploadDir = "src/main/resources/static/images/vehicles/";
                Files.createDirectories(Paths.get(uploadDir));
                String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
                Files.write(Paths.get(uploadDir + fileName), image.getBytes());
                vehicle.setImagePath("/images/vehicles/" + fileName);
            } catch (IOException e) {
                vehicle.setImagePath(existing != null ? existing.getImagePath() : "none");
            }
        } else if (imagePath != null && !imagePath.isEmpty()) {
            vehicle.setImagePath(imagePath);
        } else {
            vehicle.setImagePath(existing != null ? existing.getImagePath() : "none");
        }

        vehicleService.updateVehicle(vehicle);
        return "redirect:/vehicles";
    }

    @PostMapping("/vehicles/delete/{vehicleId}")
    public String deleteVehicle(@PathVariable String vehicleId, HttpSession session) {
        User loggedIn = (User) session.getAttribute("loggedInUser");
        if (loggedIn == null) return "redirect:/login";
        String role = loggedIn.getRole();
        if (!"ADMIN".equals(role) && !"SUPER_ADMIN".equals(role)) return "redirect:/vehicles";
        vehicleService.deleteVehicle(vehicleId);
        return "redirect:/vehicles";
    }

    @GetMapping("/vehicles/filter/type/{type}")
    public String filterByType(@PathVariable String type, Model model, HttpSession session) {
        model.addAttribute("user", session.getAttribute("loggedInUser"));
        List<Vehicle> all = vehicleService.getAllVehicles();
        List<Vehicle> filtered = all.stream()
                .filter(v -> v.getType().equalsIgnoreCase(type))
                .collect(java.util.stream.Collectors.toList());
        model.addAttribute("vehicles", filtered);
        model.addAttribute("filtered", type);
        return "vehicle/index";
    }

    @GetMapping("/vehicles/sort/price")
    public String sortByPrice(Model model, HttpSession session) {
        model.addAttribute("user", session.getAttribute("loggedInUser"));
        model.addAttribute("vehicles", vehicleService.getVehiclesSortedByPrice());
        model.addAttribute("sorted", "price");
        return "vehicle/index";
    }

    @GetMapping("/vehicles/sort/availability")
    public String sortByAvailability(Model model, HttpSession session) {
        model.addAttribute("user", session.getAttribute("loggedInUser"));
        model.addAttribute("vehicles", vehicleService.getVehiclesSortedByAvailability());
        model.addAttribute("sorted", "availability");
        return "vehicle/index";
    }
}