package com.vehiclerental.controller;

import com.vehiclerental.model.*;
import com.vehiclerental.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/vehicles")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    // ─── LIST ALL ───────────────────────────────
    @GetMapping
    public String listVehicles(Model model) throws IOException {
        model.addAttribute("vehicles", vehicleService.getAllVehicles());
        return "vehicles/list";
    }

    // ─── ADD FORM ────────────────────────────────
    @GetMapping("/add")
    public String showAddForm(Model model) {
        return "vehicles/add";
    }

    // ─── ADD SUBMIT ──────────────────────────────
    @PostMapping("/add")
    public String addVehicle(@RequestParam String type,
                             @RequestParam String brand,
                             @RequestParam String model,
                             @RequestParam int year,
                             @RequestParam double rentalPricePerDay,
                             @RequestParam(defaultValue = "true") boolean available,
                             @RequestParam String description,
                             RedirectAttributes redirectAttrs) throws IOException {
        Vehicle vehicle = switch (type.toUpperCase()) {
            case "VAN"  -> new Van(null, brand, model, year, rentalPricePerDay, available, description);
            case "BIKE" -> new Bike(null, brand, model, year, rentalPricePerDay, available, description);
            default     -> new Car(null, brand, model, year, rentalPricePerDay, available, description);
        };
        vehicleService.addVehicle(vehicle);
        redirectAttrs.addFlashAttribute("success", "Vehicle added successfully!");
        return "redirect:/vehicles";
    }

    // ─── EDIT FORM ───────────────────────────────
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable String id, Model model) throws IOException {
        Optional<Vehicle> vehicle = vehicleService.getVehicleById(id);
        if (vehicle.isEmpty()) return "redirect:/vehicles";
        model.addAttribute("vehicle", vehicle.get());
        return "vehicles/edit";
    }

    // ─── EDIT SUBMIT ─────────────────────────────
    @PostMapping("/edit/{id}")
    public String updateVehicle(@PathVariable String id,
                                @RequestParam String type,
                                @RequestParam String brand,
                                @RequestParam String model,
                                @RequestParam int year,
                                @RequestParam double rentalPricePerDay,
                                @RequestParam(defaultValue = "false") boolean available,
                                @RequestParam String description,
                                RedirectAttributes redirectAttrs) throws IOException {
        Vehicle updated = switch (type.toUpperCase()) {
            case "VAN"  -> new Van(id, brand, model, year, rentalPricePerDay, available, description);
            case "BIKE" -> new Bike(id, brand, model, year, rentalPricePerDay, available, description);
            default     -> new Car(id, brand, model, year, rentalPricePerDay, available, description);
        };
        vehicleService.updateVehicle(updated);
        redirectAttrs.addFlashAttribute("success", "Vehicle updated successfully!");
        return "redirect:/vehicles";
    }

    // ─── DELETE ──────────────────────────────────
    @PostMapping("/delete/{id}")
    public String deleteVehicle(@PathVariable String id,
                                RedirectAttributes redirectAttrs) throws IOException {
        vehicleService.deleteVehicle(id);
        redirectAttrs.addFlashAttribute("success", "Vehicle removed successfully!");
        return "redirect:/vehicles";
    }

    // ─── SEARCH ──────────────────────────────────
    @GetMapping("/search")
    public String searchVehicles(@RequestParam(required = false) String type,
                                 @RequestParam(required = false) Double maxPrice,
                                 @RequestParam(required = false) Boolean available,
                                 Model model) throws IOException {
        List<Vehicle> results = vehicleService.searchVehicles(type, maxPrice, available);
        model.addAttribute("vehicles", results);
        model.addAttribute("type", type);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("available", available);
        return "vehicles/search";
    }

    // ─── HOME REDIRECT ────────────────────────────
    @GetMapping("/")
    public String home() {
        return "redirect:/vehicles";
    }
}
