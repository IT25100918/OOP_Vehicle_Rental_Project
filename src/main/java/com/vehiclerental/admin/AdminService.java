package com.vehiclerental.admin;

import com.vehiclerental.shared.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AdminService {

    @Autowired private AdminRepository adminRepository;

    public boolean addAdmin(Admin admin) {
        if (findByEmail(admin.getEmail()) != null) return false;
        admin.setAdminId("A" + UUID.randomUUID().toString().replace("-", "").substring(0, 12));
        // Hash password before saving
        admin.setPassword(PasswordUtil.hash(admin.getPassword()));
        return adminRepository.append(admin);
    }

    public List<Admin> getAllAdmins() { return adminRepository.readAll(); }

    public Admin findByEmail(String email) {
        return getAllAdmins().stream()
                .filter(a -> a.getEmail().trim().equalsIgnoreCase(email.trim()))
                .findFirst().orElse(null);
    }

    public Admin findById(String adminId) {
        return getAllAdmins().stream()
                .filter(a -> a.getAdminId().trim().equals(adminId.trim()))
                .findFirst().orElse(null);
    }

    public Admin loginAdmin(String email, String password) {
        return getAllAdmins().stream()
                .filter(a -> a.getEmail().trim().equalsIgnoreCase(email.trim())
                          && PasswordUtil.matches(password.trim(), a.getPassword().trim())
                          && "Active".equalsIgnoreCase(a.getStatus().trim()))
                .findFirst().orElse(null);
    }

    public List<Admin> getAdminsSortedByRole() {
        List<Admin> admins = getAllAdmins();
        int n = admins.size();
        for (int i = 0; i < n - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < n; j++)
                if (admins.get(j).getRole().compareTo(admins.get(minIdx).getRole()) < 0) minIdx = j;
            Admin tmp = admins.get(minIdx);
            admins.set(minIdx, admins.get(i));
            admins.set(i, tmp);
        }
        return admins;
    }

    public boolean updateAdmin(Admin updated) {
        List<Admin> admins = getAllAdmins();
        boolean found = false;
        for (int i = 0; i < admins.size(); i++)
            if (admins.get(i).getAdminId().equals(updated.getAdminId())) {
                admins.set(i, updated); found = true; break;
            }
        if (found) adminRepository.saveAll(admins);
        return found;
    }

    /** Change password — accepts raw new password, hashes before saving. */
    public boolean changePassword(String adminId, String rawNewPassword) {
        List<Admin> admins = getAllAdmins();
        boolean found = false;
        for (Admin a : admins)
            if (a.getAdminId().equals(adminId)) {
                a.setPassword(PasswordUtil.hash(rawNewPassword));
                found = true; break;
            }
        if (found) adminRepository.saveAll(admins);
        return found;
    }

    public boolean toggleStatus(String adminId) {
        List<Admin> admins = getAllAdmins();
        boolean found = false;
        for (Admin a : admins)
            if (a.getAdminId().equals(adminId)) {
                a.setStatus("Active".equals(a.getStatus()) ? "Inactive" : "Active");
                found = true; break;
            }
        if (found) adminRepository.saveAll(admins);
        return found;
    }

    public boolean deleteAdmin(String adminId) {
        List<Admin> admins = getAllAdmins();
        boolean removed = admins.removeIf(a -> a.getAdminId().equals(adminId));
        if (removed) adminRepository.saveAll(admins);
        return removed;
    }
}
