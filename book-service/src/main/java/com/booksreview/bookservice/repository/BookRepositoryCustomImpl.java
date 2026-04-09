package com.booksreview.bookservice.repository;

import com.booksreview.bookservice.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class BookRepositoryCustomImpl implements BookRepositoryCustom {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<Book> advancedSearch(String title, String genre, String summary) {
        Query query = new Query();
        List<Criteria> criteriaList = new ArrayList<>();

        if (title != null && !title.isBlank()) {
            criteriaList.add(Criteria.where("title").regex(".*" + title + ".*", "i"));
        }

        if (genre != null && !genre.isBlank()) {
            criteriaList.add(Criteria.where("genre").regex(".*" + genre + ".*", "i"));
        }

        if (summary != null && !summary.isBlank()) {
            criteriaList.add(Criteria.where("summary").regex(".*" + summary + ".*", "i"));
        }

        if (!criteriaList.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));
        }

        return mongoTemplate.find(query, Book.class);
    }
}
