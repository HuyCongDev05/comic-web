package com.example.projectrestfulapi.dto.response.Comment;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
public class CommentResponseDTO {
    private String comicUuid;
    private String accountUuid;
    private String fullName;
    private String avatarAccount;
    private BigDecimal rating;
    private String message;
    private Instant time;
}
