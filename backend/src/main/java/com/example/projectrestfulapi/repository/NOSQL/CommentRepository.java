package com.example.projectrestfulapi.repository.NOSQL;

import com.example.projectrestfulapi.domain.NOSQL.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends MongoRepository<Comment, String> {
}
