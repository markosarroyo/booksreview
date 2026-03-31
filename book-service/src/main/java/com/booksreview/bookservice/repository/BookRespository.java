package com.booksreview.bookservice.repository;

import com.booksreview.bookservice.model.Book;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRespository extends MongoRepository<Book, String> {
    List<Book> findByGenre(String genre);

    List<Book> findByAuthorId(String id);

    List<Book> findByTittleIgnoreCase(String title);

}
