package com.example.projectrestfulapi.domain.NOSQL;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Document("messages")
public class Message {
    @Id
    private String id;
    private String chatType;
    private String groupId;
    private String accountUuidSend;
    private String fullNameAccountSend;
    private String avatarAccountSend;
    private String accountUuidReceive;
    private String message;
    private Instant time;

    public Message() {
        this.time = Instant.now();
    }
}
