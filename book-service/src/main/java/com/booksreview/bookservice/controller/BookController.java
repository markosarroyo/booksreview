package com.booksreview.bookservice.controller;

import com.booksreview.bookservice.dto.BookRequestDTO;
import com.booksreview.bookservice.dto.BookResponseDTO;
import com.booksreview.bookservice.dto.BookWithAuthorDTO;
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

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public List<BookResponseDTO> findAll() {
        return bookService.findAll().stream()
                .map(bookService::toResponseDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponseDTO> findById(@PathVariable String id) {
        return bookService.findById(id)
                .map(bookService::toResponseDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/genre/{genre}")
    public List<BookResponseDTO> findByGenre(@PathVariable String genre) {
        return bookService.findByGenre(genre).stream()
                .map(bookService::toResponseDTO)
                .toList();
    }

    @GetMapping("/author/{authorId}")
    public List<BookResponseDTO> findByAuthor(@PathVariable String authorId) {
        return bookService.findByAuthorId(authorId).stream()
                .map(bookService::toResponseDTO)
                .toList();
    }

    @GetMapping("/search")
    public List<BookResponseDTO> search(@RequestParam String title) {
        return bookService.findByTitle(title).stream()
                .map(bookService::toResponseDTO)
                .toList();
    }

    @PostMapping
    public ResponseEntity<BookResponseDTO> create(@Valid @RequestBody BookRequestDTO dto) {
        return ResponseEntity.status(201).body(bookService.save(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookResponseDTO> update(@PathVariable String id,
                                                  @Valid @RequestBody BookRequestDTO dto) {
        return ResponseEntity.ok(bookService.update(id, dto));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        bookService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/advanced-search")
    public List<BookWithAuthorDTO> advancedSearch(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String summary,
            @RequestParam(required = false) String authorName) {
        return bookService.advancedSearch(title, genre, summary, authorName);
    }
}