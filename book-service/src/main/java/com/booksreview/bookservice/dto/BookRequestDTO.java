package com.booksreview.bookservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class BookRequestDTO {

    @NotBlank(message = "Please provide the title of the book")
    @Size(min = 2, max = 100, message = "Title must be between 2 and 100 characters")
    private String title;

    @NotBlank(message = "Please provide a summary for the book")
    @Size(min = 10, max = 1000, message = "Summary must be between 10 and 1000 characters")
    private String summary;

    @NotBlank(message = "Please inform the book's genre")
    @Size(min = 3, max = 50, message = "Genre must be between 3 and 50 characters")
    private String genre;

    @Pattern(
            regexp = "^(https?://.*)?$",
            message = "coverUrl must be a valid URL starting with http:// or https://"
    )
    private String coverUrl;

    @NotBlank(message = "Author is mandatory")
    private String authorId;
}