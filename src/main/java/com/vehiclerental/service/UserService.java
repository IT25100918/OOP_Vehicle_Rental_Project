package com.vehiclerental.service;

import com.vehiclerental.algorithm.SelectionSort;
import com.vehiclerental.linkedlist.LinkedList;
import com.vehiclerental.model.User;
import com.vehiclerental.util.FileHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private static final String FILE = "users.txt";
    private final FileHandler fileHandler;

    @Autowired
    public UserService(FileHandler fileHandler) {
        this.fileHandler = fileHandler;
        seedDefaultAdmin();
    }

    // Load all users from file into LinkedList
    private LinkedList<User> loadAll() {
        LinkedList<User> list = new LinkedList<>();
        for (String line : fileHandler.readLines(FILE)) {
            User u = User.fromCsv(line);
            if (u != null) list.addLast(u);
        }
        return list;
    }

    // Save all users from LinkedList back to file
    private void saveAll(LinkedList<User> list) {
        List<String> lines = new ArrayList<>();
        for (User u : list.toList()) lines.add(u.toCsv());
        fileHandler.writeLines(FILE, lines);
    }

    // ─── CRUD Operations ────────────────────────────────────────────────────

    /** CREATE: Register a new user */
    public boolean register(User user) {
        LinkedList<User> list = loadAll();
        // Check duplicate email
        for (User u : list.toList()) {
            if (u.getEmail().equalsIgnoreCase(user.getEmail())) return false;
        }
        String id = fileHandler.generateId("USR", FILE);
        user.setUserId(id);
        list.addLast(user);
        saveAll(list);
        return true;
    }

    /** READ: Get all users sorted by name */
    public List<User> getAllUsers() {
        List<User> users = loadAll().toList();
        SelectionSort.sortUsersByName(users); // Apply Selection Sort
        return users;
    }

    /** READ: Find user by ID */
    public User findById(String userId) {
        for (User u : loadAll().toList()) {
            if (u.getUserId().equals(userId)) return u;
        }
        return null;
    }

    /** READ: Find user by email */
    public User findByEmail(String email) {
        for (User u : loadAll().toList()) {
            if (u.getEmail().equalsIgnoreCase(email)) return u;
        }
        return null;
    }

    /** READ: Authenticate user (login) */
    public User authenticate(String email, String password) {
        User user = findByEmail(email);
        if (user != null && user.getPassword().equals(password)) return user;
        return null;
    }

    /** UPDATE: Modify user details */
    public boolean update(User updated) {
        LinkedList<User> list = loadAll();
        List<User> users = list.toList();
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUserId().equals(updated.getUserId())) {
                list.set(i, updated);
                saveAll(list);
                return true;
            }
        }
        return false;
    }

    /** DELETE: Remove user by ID */
    public boolean delete(String userId) {
        LinkedList<User> list = loadAll();
        List<User> users = list.toList();
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUserId().equals(userId)) {
                list.deleteByIndex(i);
                saveAll(list);
                return true;
            }
        }
        return false;
    }

    /** Get only customer users */
    public List<User> getCustomers() {
        List<User> result = new ArrayList<>();
        for (User u : getAllUsers()) {
            if ("customer".equalsIgnoreCase(u.getUserType())) result.add(u);
        }
        return result;
    }

    /** Seed default admin if no users exist */
    private void seedDefaultAdmin() {
        if (!fileHandler.fileExists(FILE) || fileHandler.readLines(FILE).isEmpty()) {
            User admin = new User("USR001", "Admin", "000000000V", "0771234567",
                    "admin@vrental.com", "admin123", "admin");
            fileHandler.appendLine(FILE, admin.toCsv());
        }
    }
}
