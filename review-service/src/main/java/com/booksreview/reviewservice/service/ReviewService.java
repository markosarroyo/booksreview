package com.booksreview.reviewservice.service;

import com.booksreview.reviewservice.model.Review;
import com.booksreview.reviewservice.respository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public List<Review> findByBookId(String bookId) {
        return reviewRepository.findByBookId(bookId);
    }

    public List<Review> findByUserId(String userId) {
        return reviewRepository.findByBookId(userId);
    }

    public Review save(Review review) {
        review.setCreatedAt(LocalDateTime.now());
        return reviewRepository.save(review);
    }

    public void deleteById(String id) {
        reviewRepository.deleteById(id);
    }

    public double getAverageRating(String bookId) {
        List<Review> reviews = reviewRepository.findByBookId(bookId);
        return reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);
    }

}
