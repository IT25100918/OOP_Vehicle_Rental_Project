package com.vehiclerental.service;

import com.vehiclerental.algorithm.SelectionSort;
import com.vehiclerental.linkedlist.LinkedList;
import com.vehiclerental.model.Feedback;
import com.vehiclerental.util.FileHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class FeedbackService {

    private static final String FILE = "reviews.txt";
    private final FileHandler fileHandler;

    @Autowired
    public FeedbackService(FileHandler fileHandler) {
        this.fileHandler = fileHandler;
    }

    private LinkedList<Feedback> loadAll() {
        LinkedList<Feedback> list = new LinkedList<>();
        for (String line : fileHandler.readLines(FILE)) {
            Feedback f = Feedback.fromCsv(line);
            if (f != null) list.addLast(f);
        }
        return list;
    }

    private void saveAll(LinkedList<Feedback> list) {
        List<String> lines = new ArrayList<>();
        for (Feedback f : list.toList()) lines.add(f.toCsv());
        fileHandler.writeLines(FILE, lines);
    }

    // ─── CRUD Operations ────────────────────────────────────────────────────

    /** CREATE: Submit a review */
    public boolean addFeedback(Feedback feedback) {
        feedback.setFeedbackId(fileHandler.generateId("REV", FILE));
        feedback.setStatus("active");
        feedback.setCreatedAt(LocalDate.now().toString());

        LinkedList<Feedback> list = loadAll();
        list.addLast(feedback);
        saveAll(list);
        return true;
    }

    /** READ: All active feedback sorted by rating */
    public List<Feedback> getAllFeedback() {
        List<Feedback> result = new ArrayList<>();
        for (Feedback f : loadAll().toList()) {
            if ("active".equalsIgnoreCase(f.getStatus())) result.add(f);
        }
        SelectionSort.sortFeedbackByRating(result); // Highest rated first
        return result;
    }

    /** READ: All feedback for admin (including hidden) */
    public List<Feedback> getAllFeedbackAdmin() {
        List<Feedback> result = loadAll().toList();
        SelectionSort.sortFeedbackByRating(result);
        return result;
    }

    /** READ: Feedback by vehicle */
    public List<Feedback> getFeedbackByVehicle(String vehicleId) {
        List<Feedback> result = new ArrayList<>();
        for (Feedback f : loadAll().toList()) {
            if (f.getVehicleId().equals(vehicleId) && "active".equals(f.getStatus())) {
                result.add(f);
            }
        }
        SelectionSort.sortFeedbackByRating(result);
        return result;
    }

    /** READ: Find by ID */
    public Feedback findById(String feedbackId) {
        for (Feedback f : loadAll().toList()) {
            if (f.getFeedbackId().equals(feedbackId)) return f;
        }
        return null;
    }

    /** UPDATE: Edit a review */
    public boolean update(Feedback updated) {
        LinkedList<Feedback> list = loadAll();
        List<Feedback> feedbacks = list.toList();
        for (int i = 0; i < feedbacks.size(); i++) {
            if (feedbacks.get(i).getFeedbackId().equals(updated.getFeedbackId())) {
                list.set(i, updated);
                saveAll(list);
                return true;
            }
        }
        return false;
    }

    /** UPDATE: Toggle hide/show a review (admin moderation) */
    public boolean toggleStatus(String feedbackId) {
        Feedback f = findById(feedbackId);
        if (f == null) return false;
        f.setStatus("active".equals(f.getStatus()) ? "hidden" : "active");
        return update(f);
    }

    /** DELETE: Remove a review */
    public boolean delete(String feedbackId) {
        LinkedList<Feedback> list = loadAll();
        List<Feedback> feedbacks = list.toList();
        for (int i = 0; i < feedbacks.size(); i++) {
            if (feedbacks.get(i).getFeedbackId().equals(feedbackId)) {
                list.deleteByIndex(i);
                saveAll(list);
                return true;
            }
        }
        return false;
    }

    /** Average rating across all reviews */
    public double getAverageRating() {
        List<Feedback> all = getAllFeedback();
        if (all.isEmpty()) return 0;
        return all.stream().mapToInt(Feedback::getRating).average().orElse(0);
    }
}
