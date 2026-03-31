package com.booksreview.reviewservice.respository;

import com.booksreview.reviewservice.model.Review;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends MongoRepository<Review, String> {
    List<Review> findByBookId(String bookId);

    List<Review> findByUserId(String userId);

    void deleteByBookId(String bookId);

}
