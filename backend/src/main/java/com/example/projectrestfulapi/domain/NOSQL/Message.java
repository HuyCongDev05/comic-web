package com.example.projectrestfulapi.domain.NOSQL;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Getter
@Setter
@Document("messages")
public class Message {
    @Id
    private String id;
    private String chatType;
    private String groupId;
    private String userId;
    private String userSend;
    private String avatarUserSend;
    private String userReceive;
    private String message;
    private Instant time;
    private String status;

    public Message() {
        this.time = Instant.now();
    }
}
