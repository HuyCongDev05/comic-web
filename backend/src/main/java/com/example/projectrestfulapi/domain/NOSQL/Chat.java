package com.example.projectrestfulapi.domain.NOSQL;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Document("chats")
public class Chat {
    @Id
    private String chatId;

    private String chatType;

    private List<String> participants;

    private String groupName;

    private String groupAvatar;

    private Instant updatedAt = Instant.now();
}
