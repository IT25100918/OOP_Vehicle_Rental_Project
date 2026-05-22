package com.vehiclerental.admin;

import com.vehiclerental.shared.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

@Controller
public class AdminController {

    @Autowired private AdminService adminService;

    /** Safely extract the role from whatever object is in session (User or Admin). */
    private String sessionRole(HttpSession session) {
        Object loggedIn = session.getAttribute("loggedInUser");
        if (loggedIn instanceof com.vehiclerental.customer.User u) return u.getRole();
        if (loggedIn instanceof Admin a) return a.getRole();
        return "";
    }

    @GetMapping("/admins")
    public String listAdmins(Model model, HttpSession session) {
        Object loggedIn = session.getAttribute("loggedInUser");
        if (loggedIn == null) return "redirect:/login";
        String role = sessionRole(session);
        if (!"ADMIN".equals(role) && !"SUPER_ADMIN".equals(role))
            return "redirect:/dashboard";
        model.addAttribute("user", loggedIn);
        model.addAttribute("admins", adminService.getAllAdmins());
        return "admin/index";
    }

    @GetMapping("/admins/add")
    public String showAddForm(HttpSession session, Model model) {
        Object loggedIn = session.getAttribute("loggedInUser");
        if (loggedIn == null) return "redirect:/login";
        String role = sessionRole(session);
        if (!"ADMIN".equals(role) && !"SUPER_ADMIN".equals(role))
            return "redirect:/dashboard";
        model.addAttribute("user", loggedIn);
        return "admin/add-admin";
    }

    @PostMapping("/admins/add")
    public String addAdmin(
            @RequestParam String fullName, @RequestParam String email,
            @RequestParam String password, @RequestParam String phoneNumber,
            @RequestParam String role, Model model, HttpSession session) {
        Admin admin = new Admin();
        admin.setFullName(fullName); admin.setEmail(email);
        admin.setPassword(password); admin.setPhoneNumber(phoneNumber);
        admin.setRole(role); admin.setStatus("Active");
        if (adminService.addAdmin(admin)) return "redirect:/admins";
        model.addAttribute("user", session.getAttribute("loggedInUser"));
        model.addAttribute("error", "Email already exists!");
        return "admin/add-admin";
    }

    @GetMapping("/admins/edit/{adminId}")
    public String showEditForm(@PathVariable String adminId, Model model, HttpSession session) {
        if (session.getAttribute("loggedInUser") == null) return "redirect:/login";
        model.addAttribute("user", session.getAttribute("loggedInUser"));
        model.addAttribute("admin", adminService.findById(adminId));
        return "admin/edit-admin";
    }

    @PostMapping("/admins/update")
    public String updateAdmin(
            @RequestParam String adminId, @RequestParam String fullName,
            @RequestParam String phoneNumber, @RequestParam String role) {
        Admin admin = adminService.findById(adminId);
        if (admin == null) return "redirect:/admins";
        admin.setFullName(fullName); admin.setPhoneNumber(phoneNumber); admin.setRole(role);
        adminService.updateAdmin(admin);
        return "redirect:/admins";
    }

    @PostMapping("/admins/change-password")
    public String changePassword(
            @RequestParam String adminId, @RequestParam String currentPassword,
            @RequestParam String newPassword, @RequestParam String confirmPassword,
            Model model, HttpSession session) {
        Admin admin = adminService.findById(adminId);
        model.addAttribute("user", session.getAttribute("loggedInUser"));
        model.addAttribute("admin", admin);
        if (admin == null) { model.addAttribute("pwError", "Admin not found."); return "admin/edit-admin"; }
        if (!PasswordUtil.matches(currentPassword, admin.getPassword())) {
            model.addAttribute("pwError", "Current password is incorrect."); return "admin/edit-admin";
        }
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("pwError", "New passwords do not match."); return "admin/edit-admin";
        }
        adminService.changePassword(adminId, newPassword);
        model.addAttribute("pwMessage", "Password updated successfully.");
        return "admin/edit-admin";
    }

    // Changed to POST to prevent CSRF via GET
    @PostMapping("/admins/toggle/{adminId}")
    public String toggleStatus(@PathVariable String adminId, HttpSession session) {
        if (session.getAttribute("loggedInUser") == null) return "redirect:/login";
        String role = sessionRole(session);
        if (!"ADMIN".equals(role) && !"SUPER_ADMIN".equals(role)) return "redirect:/admins";
        adminService.toggleStatus(adminId);
        return "redirect:/admins";
    }

    // Changed to POST to prevent CSRF via GET
    @PostMapping("/admins/delete/{adminId}")
    public String deleteAdmin(@PathVariable String adminId, HttpSession session) {
        if (session.getAttribute("loggedInUser") == null) return "redirect:/login";
        String role = sessionRole(session);
        if (!"ADMIN".equals(role) && !"SUPER_ADMIN".equals(role)) return "redirect:/admins";
        adminService.deleteAdmin(adminId);
        return "redirect:/admins";
    }

    @GetMapping("/admins/sort/role")
    public String sortByRole(Model model, HttpSession session) {
        model.addAttribute("user", session.getAttribute("loggedInUser"));
        model.addAttribute("admins", adminService.getAdminsSortedByRole());
        return "admin/index";
    }
}
