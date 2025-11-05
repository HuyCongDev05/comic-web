package com.example.projectrestfulapi.repository.NOSQL;

import com.example.projectrestfulapi.domain.NOSQL.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends MongoRepository<Message, String> {
}
