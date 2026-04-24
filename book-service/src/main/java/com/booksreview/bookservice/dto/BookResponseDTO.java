package com.booksreview.bookservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookResponseDTO {
    private String id;
    private String title;
    private String summary;
    private String genre;
    private String coverUrl;
    private String authorId;
}