package com.vehiclerental.shared;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Central BCrypt password utility.
 * Passwords are hashed before storage and verified by bcrypt comparison.
 */
public class PasswordUtil {

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public static String hash(String rawPassword) {
        return encoder.encode(rawPassword);
    }

    public static boolean matches(String rawPassword, String hashedPassword) {
        // Support legacy plaintext passwords during migration: if stored value
        // is not a bcrypt hash (doesn't start with $2), fall back to equals.
        if (hashedPassword != null && !hashedPassword.startsWith("$2")) {
            return rawPassword.equals(hashedPassword);
        }
        return encoder.matches(rawPassword, hashedPassword);
    }
}
