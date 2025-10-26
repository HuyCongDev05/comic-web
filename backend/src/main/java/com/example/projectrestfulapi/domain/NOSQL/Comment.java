package com.example.projectrestfulapi.domain.NOSQL;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Document("comments")
public class Comment {
    @Id
    private String id;

    @NotBlank(message = "Cannot be left blank comicUuid")
    private String comicUuid;

    @NotBlank(message = "Cannot be left blank accountUuid")
    private String accountUuid;

    @NotBlank(message = "Cannot be left blank fullName")
    private String fullName;

    private String avatarAccount;

    @NotBlank(message = "Cannot be left blank rating")
    private BigDecimal rating;

    @NotBlank(message = "Cannot be left blank message")
    private String message;

    private Instant time;

    public Comment() {
        this.time = Instant.now();
    }
}
