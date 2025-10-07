package com.example.projectrestfulapi.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class FormatResponseDTO<T> {
    private boolean success = true;
    private int status;
    private String message;
    private T data;
    private String timestamp = Instant.now().toString();
}
