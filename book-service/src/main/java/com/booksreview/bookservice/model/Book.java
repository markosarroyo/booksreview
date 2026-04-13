package com.booksreview.bookservice.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document (collection="books")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Book {
    @Id
    String id;

    @NotBlank(message = "Please provide the title of the book")
    @Size(min = 2, max = 100, message = "Title must be between 2 and 100 characters")
    String title;

    @NotBlank(message ="Please provide a summary for the book")
    @Size(min = 10, max = 1000, message = "Summary must be between 10 and 1000 characters")
    String summary;

    @NotBlank(message = "Please inform the book's genre")
    @Size(min = 3, max = 50, message = "Genre must be between 3 and 50 characters")
    String genre;

    @Pattern(
            regexp = "^(https?://.*)?$",
            message = "coverUrl must be a valid URL starting with http:// or https://"
    )
    String coverUrl;

    @NotBlank(message ="Author is mandatory")
    String authorId;
}
