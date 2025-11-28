package com.example.projectrestfulapi.repository.NOSQL;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface MessageRepository extends MongoRepository<Message, String> {
}