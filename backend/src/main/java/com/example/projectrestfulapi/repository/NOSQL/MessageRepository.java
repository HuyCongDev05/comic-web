package com.example.projectrestfulapi.repository.NOSQL;

import com.example.projectrestfulapi.domain.NOSQL.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends MongoRepository<Message, String> {
    List<Message> findByUserSend(String userSend);

    List<Message> findAllByUserSendAndGroupId(String userSend, String groupId);
}
