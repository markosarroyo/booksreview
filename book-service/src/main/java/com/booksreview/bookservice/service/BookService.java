package com.booksreview.bookservice.service;

import com.booksreview.bookservice.model.Book;
import com.booksreview.bookservice.repository.BookRespository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    private final BookRespository bookRespository;

    public BookService(BookRespository bookRespository) {
        this.bookRespository = bookRespository;
    }

    public List<Book> findAll() {
        return bookRespository.findAll();
    }

    public Optional<Book> findById(String id) {
        return bookRespository.findById(id);
    }

    public List<Book> findByAuthorId(String authorId) {
        return bookRespository.findByAuthorId(authorId);
    }

    public List<Book> findByTitle(String title) {
        return bookRespository.findByTittleIgnoreCase(title);
    }

    public List<Book> findByGenre(String genre) {
        return bookRespository.findByGenre(genre);
    }

    public Book save(Book book) {
        return bookRespository.save(book);
    }

    public void deleteById(String id) {
        bookRespository.deleteById(id);

    }


}
