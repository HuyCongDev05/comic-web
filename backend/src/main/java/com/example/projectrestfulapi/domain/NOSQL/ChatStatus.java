package com.example.projectrestfulapi.domain.NOSQL;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Getter
@Setter
@Document("chat_status")
public class ChatStatus {
    @Id
    private String id;

    private String userId;
    private String chatId;

    private String lastSeenMessageId;
    private int unreadCount = 0;

    private Instant updatedAt = Instant.now();
}
