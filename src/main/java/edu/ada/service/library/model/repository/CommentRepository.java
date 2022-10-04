package edu.ada.service.library.model.repository;

import edu.ada.service.library.model.entity.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

//Mongo Repository
@Repository
public interface CommentRepository extends MongoRepository<Comment, String> {
    List<Comment> findByBookId(Long bookId);
}
