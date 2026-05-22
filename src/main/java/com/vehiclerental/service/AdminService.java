package com.vehiclerental.service;

import com.vehiclerental.algorithm.SelectionSort;
import com.vehiclerental.linkedlist.LinkedList;
import com.vehiclerental.model.AdminUser;
import com.vehiclerental.model.User;
import com.vehiclerental.util.FileHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class AdminService {

    private static final String FILE = "admins.txt";
    private static final String LOG_FILE = "activity_log.txt";
    private final FileHandler fileHandler;

    @Autowired
    public AdminService(FileHandler fileHandler) {
        this.fileHandler = fileHandler;
        seedDefaultAdmin();
    }

    private LinkedList<AdminUser> loadAll() {
        LinkedList<AdminUser> list = new LinkedList<>();
        for (String line : fileHandler.readLines(FILE)) {
            String[] parts = line.split(",", -1);
            if (parts.length < 9) continue;
            AdminUser admin = new AdminUser(
                parts[0], parts[1], parts[2], parts[3], parts[4], parts[5],
                parts[7], parts[8]
            );
            list.addLast(admin);
        }
        return list;
    }

    private void saveAll(LinkedList<AdminUser> list) {
        List<String> lines = new ArrayList<>();
        for (AdminUser a : list.toList()) lines.add(a.toCsv());
        fileHandler.writeLines(FILE, lines);
    }

    // ─── CRUD Operations ────────────────────────────────────────────────────

    /** CREATE: Register a new admin account */
    public boolean register(AdminUser admin) {
        LinkedList<AdminUser> list = loadAll();
        for (AdminUser a : list.toList()) {
            if (a.getEmail().equalsIgnoreCase(admin.getEmail())) return false;
        }
        admin.setUserId(fileHandler.generateId("ADM", FILE));
        list.addLast(admin);
        saveAll(list);
        logActivity("SYSTEM", "Admin registered: " + admin.getEmail());
        return true;
    }

    /** READ: All admin accounts sorted by role */
    public List<AdminUser> getAllAdmins() {
        List<AdminUser> admins = loadAll().toList();
        // Sort by role alphabetically using selection sort
        int n = admins.size();
        for (int i = 0; i < n - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < n; j++) {
                if (admins.get(j).getRole().compareToIgnoreCase(admins.get(minIdx).getRole()) < 0)
                    minIdx = j;
            }
            AdminUser temp = admins.get(minIdx);
            admins.set(minIdx, admins.get(i));
            admins.set(i, temp);
        }
        return admins;
    }

    /** READ: Find admin by ID */
    public AdminUser findById(String adminId) {
        for (AdminUser a : loadAll().toList()) {
            if (a.getUserId().equals(adminId)) return a;
        }
        return null;
    }

    /** READ: Find admin by email */
    public AdminUser findByEmail(String email) {
        for (AdminUser a : loadAll().toList()) {
            if (a.getEmail().equalsIgnoreCase(email)) return a;
        }
        return null;
    }

    /** UPDATE: Modify admin details */
    public boolean update(AdminUser updated) {
        LinkedList<AdminUser> list = loadAll();
        List<AdminUser> admins = list.toList();
        for (int i = 0; i < admins.size(); i++) {
            if (admins.get(i).getUserId().equals(updated.getUserId())) {
                list.set(i, updated);
                saveAll(list);
                logActivity(updated.getUserId(), "Admin profile updated: " + updated.getEmail());
                return true;
            }
        }
        return false;
    }

    /** DELETE: Remove admin account */
    public boolean delete(String adminId) {
        LinkedList<AdminUser> list = loadAll();
        List<AdminUser> admins = list.toList();
        for (int i = 0; i < admins.size(); i++) {
            if (admins.get(i).getUserId().equals(adminId)) {
                logActivity("SYSTEM", "Admin removed: " + admins.get(i).getEmail());
                list.deleteByIndex(i);
                saveAll(list);
                return true;
            }
        }
        return false;
    }

    /** Log an admin activity for audit trail */
    public void logActivity(String adminId, String action) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        fileHandler.appendLine(LOG_FILE, timestamp + "," + adminId + "," + action);
    }

    /** READ: Get activity log entries */
    public List<String> getActivityLog() {
        return fileHandler.readLines(LOG_FILE);
    }

    /** Seed default admin entry in admins.txt on first run */
    private void seedDefaultAdmin() {
        if (!fileHandler.fileExists(FILE) || fileHandler.readLines(FILE).isEmpty()) {
            AdminUser admin = new AdminUser("ADM001", "Admin", "000000000V",
                "0771234567", "admin@vrental.com", "admin123", "SuperAdmin", "ALL");
            fileHandler.appendLine(FILE, admin.toCsv());
        }
    }
}
