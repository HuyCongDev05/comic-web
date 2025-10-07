package com.example.projectrestfulapi.domain.NOSQL;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Document("groups")
public class Group {
    @Id
    private String id;

    @NotBlank(message = "Cannot be left blank")
    private String groupName;

    @NotBlank(message = "Cannot be left blank")
    private String avatar;

    private Instant created;
    private List<UserJoin> users;

    private static class UserJoin {
        private String userId;
        private String role;
        private Instant joined;
    }
}
