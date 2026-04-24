package com.booksreview.reviewservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ReviewResponseDTO {
    private String id;
    private String bookId;
    private String userId;
    private String comment;
    private int rating;
    private LocalDateTime createdAt;
}