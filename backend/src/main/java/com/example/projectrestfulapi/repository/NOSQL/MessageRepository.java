package com.example.projectrestfulapi.repository.NOSQL;

import com.example.projectrestfulapi.domain.NOSQL.Message;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MessageRepository extends MongoRepository<Message, String> {
}