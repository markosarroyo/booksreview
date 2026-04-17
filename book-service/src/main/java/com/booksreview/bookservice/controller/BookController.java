package com.booksreview.bookservice.controller;

import com.booksreview.bookservice.dto.BookWithAuthorDTO;
import com.booksreview.bookservice.model.Book;
import com.booksreview.bookservice.service.BookService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookService bookService;

    public BookController (BookService bookService){
        this.bookService = bookService;
    }

    @GetMapping
    public List<Book> findAll(){
        return bookService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> findById(@PathVariable String id) {
        return bookService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/genre/{genre}")
    public List<Book> findByGenre(@PathVariable String genre) {
        return bookService.findByGenre(genre);
    }

    @GetMapping("/author/{authorId}")
    public List<Book> findByAuthor(@PathVariable String authorId) {
        return bookService.findByAuthorId(authorId);
    }

    @GetMapping("/search")
    public List<Book> search(@RequestParam String title) {
        return bookService.findByTitle(title);
    }

    @PostMapping
    public ResponseEntity<Book> create(@Valid @RequestBody Book book) {
        return ResponseEntity.status(201).body(bookService.save(book));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> update(@PathVariable String id,@Valid @RequestBody Book book) {
        book.setId(id);
        return ResponseEntity.ok(bookService.save(book));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        bookService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/advanced-search")
    public List <BookWithAuthorDTO> advancedSearch(@RequestParam(required = false) String title, @RequestParam(required = false) String genre, @RequestParam(required = false) String summary, @RequestParam(required = false)  String authorName){
        return bookService.advancedSearch(title,genre,summary, authorName);
    }
}
