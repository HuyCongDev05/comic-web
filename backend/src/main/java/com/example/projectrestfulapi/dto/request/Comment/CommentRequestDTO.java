package com.example.projectrestfulapi.dto.request.Comment;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CommentRequestDTO {

    @NotBlank(message = "Cannot be left blank accountUuid")
    private String accountUuid;

    @NotNull(message = "Rating cannot be null")
    @DecimalMin(value = "0.0", message = "Rating must be >= 0")
    @DecimalMax(value = "5.0", message = "Rating must be <= 5")
    private BigDecimal rating;

    @NotBlank(message = "Cannot be left blank message")
    private String message;
}
