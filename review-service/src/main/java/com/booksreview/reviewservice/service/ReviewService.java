package com.booksreview.reviewservice.service;

import com.booksreview.reviewservice.dto.ReviewRequestDTO;
import com.booksreview.reviewservice.dto.ReviewResponseDTO;
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
        return reviewRepository.findByUserId(userId);
    }

    public ReviewResponseDTO save(ReviewRequestDTO dto) {
        return toResponseDTO(reviewRepository.save(toEntity(dto)));
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

    public ReviewResponseDTO toResponseDTO(Review review) {
        return new ReviewResponseDTO(
                review.getId(),
                review.getBookId(),
                review.getUserId(),
                review.getComment(),
                review.getRating(),
                review.getCreatedAt()
        );
    }

    public Review toEntity(ReviewRequestDTO dto){
        Review review = new Review();
        review.setBookId(dto.getBookId());
        review.setUserId(dto.getUserId());
        review.setComment(dto.getComment());
        review.setRating(dto.getRating());
        review.setCreatedAt(LocalDateTime.now());
        return review;
    }
}