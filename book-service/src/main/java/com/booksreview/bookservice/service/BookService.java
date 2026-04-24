package com.booksreview.bookservice.service;

import com.booksreview.bookservice.client.UserServiceClient;
import com.booksreview.bookservice.dto.BookRequestDTO;
import com.booksreview.bookservice.dto.BookResponseDTO;
import com.booksreview.bookservice.dto.BookWithAuthorDTO;
import com.booksreview.bookservice.model.Book;
import com.booksreview.bookservice.repository.BookRespository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookService {
    private final BookRespository bookRespository;
    private final UserServiceClient userServiceClient;

    public BookService(BookRespository bookRespository, UserServiceClient userServiceClient) {
        this.bookRespository = bookRespository;
        this.userServiceClient = userServiceClient;
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
        return bookRespository.findByTitleIgnoreCase(title);
    }

    public List<Book> findByGenre(String genre) {
        return bookRespository.findByGenre(genre);
    }

    public BookResponseDTO save(BookRequestDTO book) {
        return toResponseDTO(bookRespository.save(toEntity(book)));
    }

    // Nuevo save para update — recibe DTO + id
    public BookResponseDTO update(String id, BookRequestDTO dto) {
        Book book = toEntity(dto);
        book.setId(id);
        return toResponseDTO(bookRespository.save(book));
    }

    public void deleteById(String id) {
        if(this.findById(id).isEmpty()){
            throw new RuntimeException("Book not found");
        }
        bookRespository.deleteById(id);
    }

    public List<BookWithAuthorDTO> advancedSearch(String title, String genre, String summary,String authorName) {
        // 1. Buscar libros por título en MongoDB
        List<Book> books = this.bookRespository.advancedSearch(title,genre,summary);

        // 2. Para cada libro, resolver el nombre del autor
        return books.stream()
                .map(book -> {
                    String name = userServiceClient.getAuthorName(book.getAuthorId());
                    // 3. Filtrar por nombre de autor si se proporcionó
                    if (authorName != null && !authorName.isBlank()) {
                        if (!name.toLowerCase().contains(authorName.toLowerCase())) {
                            return null; // no cumple el criterio
                        }
                    }
                    return new BookWithAuthorDTO(book, name);
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    // Añadir al service — convierte BookRequestDTO → Book
    private Book toEntity(BookRequestDTO dto) {
        Book book = new Book();
        book.setTitle(dto.getTitle());
        book.setSummary(dto.getSummary());
        book.setGenre(dto.getGenre());
        book.setCoverUrl(dto.getCoverUrl());
        book.setAuthorId(dto.getAuthorId());
        return book;
    }

    // Convierte Book → BookResponseDTO
    public BookResponseDTO toResponseDTO(Book book) {
        return new BookResponseDTO(
                book.getId(),
                book.getTitle(),
                book.getSummary(),
                book.getGenre(),
                book.getCoverUrl(),
                book.getAuthorId()
        );
    }

}
