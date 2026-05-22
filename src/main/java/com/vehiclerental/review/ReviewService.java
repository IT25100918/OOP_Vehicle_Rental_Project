package com.vehiclerental.review;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    @Autowired private ReviewRepository reviewRepository;

    public boolean addReview(String userId, String userName, String vehicleId,
                              String vehicleName, int rating, String comment) {
        Review review = new Review("R" + java.util.UUID.randomUUID().toString().replace("-","").substring(0,12), userId, vehicleId,
                userName, vehicleName, rating, comment, LocalDate.now().toString(), "Pending");
        return reviewRepository.append(review);
    }

    public List<Review> getAllReviews() { return reviewRepository.readAll(); }

    public Review findById(String reviewId) {
        return getAllReviews().stream()
                .filter(r -> r.getReviewId().equals(reviewId))
                .findFirst().orElse(null);
    }

    public List<Review> getReviewsSortedByRating() {
        List<Review> reviews = getAllReviews();
        int n = reviews.size();
        for (int i = 0; i < n - 1; i++) {
            int maxIdx = i;
            for (int j = i + 1; j < n; j++)
                if (reviews.get(j).getRating() > reviews.get(maxIdx).getRating()) maxIdx = j;
            Review tmp = reviews.get(maxIdx);
            reviews.set(maxIdx, reviews.get(i));
            reviews.set(i, tmp);
        }
        return reviews;
    }

    public boolean approveReview(String reviewId) {
        List<Review> reviews = getAllReviews();
        boolean found = false;
        for (Review r : reviews)
            if (r.getReviewId().equals(reviewId)) { r.setStatus("Approved"); found = true; break; }
        if (found) reviewRepository.saveAll(reviews);
        return found;
    }

    public boolean updateReview(String reviewId, String comment, int rating) {
        List<Review> reviews = getAllReviews();
        boolean found = false;
        for (Review r : reviews)
            if (r.getReviewId().equals(reviewId)) {
                r.setComment(comment); r.setRating(rating); found = true; break;
            }
        if (found) reviewRepository.saveAll(reviews);
        return found;
    }

    public boolean deleteReview(String reviewId) {
        List<Review> reviews = getAllReviews();
        boolean removed = reviews.removeIf(r -> r.getReviewId().equals(reviewId));
        if (removed) reviewRepository.saveAll(reviews);
        return removed;
    }
}
