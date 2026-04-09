package com.booksreview.bookservice.repository;

import com.booksreview.bookservice.model.Book;
import java.util.List;


public interface BookRepositoryCustom {
    List<Book> advancedSearch(String title, String genre, String summary);
}
