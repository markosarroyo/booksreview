package com.booksreview.bookservice.model;

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
    String title;
    String summary;
    String genre;
    String coverUrl;
    String authorId; // id del usuario con rol AUTHOR

}
