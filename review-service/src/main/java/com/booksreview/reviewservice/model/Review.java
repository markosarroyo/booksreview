package com.booksreview.reviewservice.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "reviews")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    @Id
    private String id;
    @NotBlank(message = "bookId mandatory")
    @Pattern(regexp = "^[a-fA-F0-9]{24}$", message = "Invalid bookId format")
    private String bookId;
    @NotBlank(message = "userId mandatory")
    @Pattern(regexp = "^[a-fA-F0-9]{24}$", message = "Invalid bookId format")
    private String userId;
    @NotBlank(message = "Provide text for the review")
    @Size(min = 5, max = 500, message = "Comment must be between 5 and 500 characters")
    @Pattern(regexp = "^(?!.*(.)\\1{5}).*$", message = "Comment contains excessive repetition")
    private String comment;
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating cannot exceed 5")
    private int rating;
    private LocalDateTime createdAt;

}
