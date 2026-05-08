package com.vehiclerental.util;

import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for reading and writing data to .txt files
 * Handles all persistence for the Vehicle Rental Platform
 */
@Component
public class FileHandler {

    private static final String DATA_DIR = "data/";

    static {
        // Ensure data directory exists on startup
        try {
            Files.createDirectories(Paths.get(DATA_DIR));
        } catch (IOException e) {
            System.err.println("Could not create data directory: " + e.getMessage());
        }
    }

    /** Read all lines from a file */
    public List<String> readLines(String filename) {
        List<String> lines = new ArrayList<>();
        Path filePath = Paths.get(DATA_DIR + filename);
        if (!Files.exists(filePath)) return lines;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    lines.add(line.trim());
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading " + filename + ": " + e.getMessage());
        }
        return lines;
    }

    /** Write all lines to a file (overwrites existing content) */
    public void writeLines(String filename, List<String> lines) {
        Path filePath = Paths.get(DATA_DIR + filename);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toFile()))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing " + filename + ": " + e.getMessage());
        }
    }

    /** Append a single line to a file */
    public void appendLine(String filename, String line) {
        Path filePath = Paths.get(DATA_DIR + filename);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toFile(), true))) {
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error appending to " + filename + ": " + e.getMessage());
        }
    }

    /** Generate a unique ID with a prefix (e.g., "USR001") */
    public String generateId(String prefix, String filename) {
        List<String> lines = readLines(filename);
        int nextNum = lines.size() + 1;
        return String.format("%s%03d", prefix, nextNum);
    }

    /** Check if a file exists */
    public boolean fileExists(String filename) {
        return Files.exists(Paths.get(DATA_DIR + filename));
    }
}
