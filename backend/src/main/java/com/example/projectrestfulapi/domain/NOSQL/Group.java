package com.example.projectrestfulapi.domain.NOSQL;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Document("groups")
public class Group {
    @Id
    private String id;

    private String groupName;

    private String avatar;

    private Instant created;
    private List<UserJoin> users = new ArrayList<>();

    public Group(String groupName, String avatar, UserJoin users) {
        this.groupName = groupName;
        this.avatar = avatar;
        this.users.add(users);
        this.created = Instant.now();
    }

    @Getter
    @Setter
    public static class UserJoin {
        private String userId;
        private String avatar;
        private String role;
        private Instant joined;
    }
}
