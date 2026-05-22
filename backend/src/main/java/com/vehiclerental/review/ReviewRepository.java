package com.vehiclerental.review;

import com.vehiclerental.shared.FileRepository;
import org.springframework.stereotype.Repository;

@Repository
public class ReviewRepository extends FileRepository<Review> {

    private static final String FILE_PATH = "data/reviews.txt";

    @Override protected String getFilePath() { return FILE_PATH; }
    @Override protected Review fromLine(String line) { return Review.fromFileString(line); }
    @Override protected String toLine(Review r) { return r.toFileString(); }
}
