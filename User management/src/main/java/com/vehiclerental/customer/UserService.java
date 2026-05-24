package com.vehiclerental.customer;

import com.vehiclerental.shared.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    @Autowired private UserRepository userRepository;

    public boolean registerUser(User user) {
        user.setEmail(user.getEmail().trim());
        if (findByEmail(user.getEmail()) != null) return false;
        user.setUserId("U" + UUID.randomUUID().toString().replace("-", "").substring(0, 12));
        // Hash password before saving
        user.setPassword(PasswordUtil.hash(user.getPassword()));
        return userRepository.append(user);
    }

    public List<User> getAllUsers() { return userRepository.readAll(); }

    public User findByEmail(String email) {
        return getAllUsers().stream()
                .filter(u -> u.getEmail().trim().equalsIgnoreCase(email.trim()))
                .findFirst().orElse(null);
    }

    public User findById(String userId) {
        return getAllUsers().stream()
                .filter(u -> u.getUserId().trim().equals(userId.trim()))
                .findFirst().orElse(null);
    }

    public User loginUser(String email, String password) {
        User user = findByEmail(email.trim());
        return (user != null && PasswordUtil.matches(password.trim(), user.getPassword().trim())) ? user : null;
    }

    public boolean updateUser(User updated) {
        List<User> users = getAllUsers();
        boolean found = false;
        for (int i = 0; i < users.size(); i++)
            if (users.get(i).getUserId().equals(updated.getUserId())) {
                users.set(i, updated); found = true; break;
            }
        if (found) userRepository.saveAll(users);
        return found;
    }

    public boolean deleteUser(String userId) {
        List<User> users = getAllUsers();
        boolean removed = users.removeIf(u -> u.getUserId().equals(userId));
        if (removed) userRepository.saveAll(users);
        return removed;
    }

    /** Reset password — hashes before saving. */
    public boolean resetPassword(String email, String newPassword) {
        List<User> users = getAllUsers();
        for (User u : users) {
            if (u.getEmail().equalsIgnoreCase(email.trim())) {
                u.setPassword(PasswordUtil.hash(newPassword));
                userRepository.saveAll(users);
                return true;
            }
        }
        return false;
    }

    /** Change password — verifies current, hashes new. Returns false if currentPassword wrong. */
    public boolean changePassword(String userId, String currentPassword, String newPassword) {
        List<User> users = getAllUsers();
        for (User u : users) {
            if (u.getUserId().equals(userId)) {
                if (!PasswordUtil.matches(currentPassword, u.getPassword())) return false;
                u.setPassword(PasswordUtil.hash(newPassword));
                userRepository.saveAll(users);
                return true;
            }
        }
        return false;
    }
}
