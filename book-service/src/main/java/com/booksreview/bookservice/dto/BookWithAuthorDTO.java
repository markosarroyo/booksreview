package com.booksreview.bookservice.dto;

import com.booksreview.bookservice.model.Book;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookWithAuthorDTO {
    private String id;
    private String title;
    private String summary;
    private String genre;
    private String authorId;
    private String authorName;

    public BookWithAuthorDTO(Book book, String authorName) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.summary = book.getSummary();
        this.genre = book.getGenre();
        this.authorId = book.getAuthorId();
        this.authorName = authorName;
    }
}