package com.example.projectrestfulapi.dto.response.user;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class UserRegisterResponseDTO {
    private String uuid;
    private String username;
    private String email;
    private Instant created;
}
