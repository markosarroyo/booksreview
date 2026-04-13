package com.booksreview.reviewservice.controller;

import com.booksreview.reviewservice.model.Review;
import com.booksreview.reviewservice.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("book/{bookId}")
    public List<Review> findByBookId(@PathVariable String bookId) {
        return reviewService.findByBookId(bookId);
    }

    @GetMapping("/user/{userId}")
    public List<Review> findByUserId(String userId) {
        return reviewService.findByUserId(userId);
    }

    @GetMapping("/book/{bookId}/rating")
    public ResponseEntity<Double> getAverageRating(@PathVariable String bookId) {
        return ResponseEntity.ok(reviewService.getAverageRating(bookId));
    }

    @PostMapping
    public ResponseEntity<Review> save(@Valid @RequestBody Review review) {
        return ResponseEntity.status(201).body(reviewService.save(review));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Review> delete(@PathVariable String id) {
        reviewService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
