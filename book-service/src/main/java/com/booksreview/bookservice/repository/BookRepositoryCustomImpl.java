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
    public List<Book> advancedSearch(String title, String author){
        Query query = new Query();
        List<Criteria> criteriaList = new ArrayList<>();

        if(title!=null && !title.isBlank()){
            criteriaList.add(
                    new Criteria.where("title").regex(".*"+title+".*","i")
            );

        }

        // Si hay criterios, los combinamos con AND
        if (!criteriaList.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));
        }
    }
}
