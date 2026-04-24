package com.booksreview.reviewservice.controller;

import com.booksreview.reviewservice.dto.ReviewRequestDTO;
import com.booksreview.reviewservice.dto.ReviewResponseDTO;
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

    @GetMapping("/book/{bookId}")
    public List<ReviewResponseDTO> findByBookId(@PathVariable String bookId) {
        return reviewService.findByBookId(bookId).stream()
                .map(reviewService::toResponseDTO)
                .toList();
    }

    @GetMapping("/user/{userId}")
    public List<ReviewResponseDTO> findByUserId(@PathVariable String userId) { // bug corregido
        return reviewService.findByUserId(userId).stream()
                .map(reviewService::toResponseDTO)
                .toList();
    }

    @GetMapping("/book/{bookId}/rating")
    public ResponseEntity<Double> getAverageRating(@PathVariable String bookId) {
        return ResponseEntity.ok(reviewService.getAverageRating(bookId));
    }

    @PostMapping
    public ResponseEntity<ReviewResponseDTO> save(@Valid @RequestBody ReviewRequestDTO dto) {
        return ResponseEntity.status(201).body(reviewService.save(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        reviewService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}