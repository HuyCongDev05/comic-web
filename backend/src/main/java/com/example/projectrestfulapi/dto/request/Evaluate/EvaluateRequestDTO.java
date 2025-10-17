package com.example.projectrestfulapi.dto.request.Evaluate;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

public class EvaluateRequestDTO {
    @Getter
    @Setter
    public static class GetRating {
        private String uuidAccount;
        private String uuidComic;
    }

    @Getter
    @Setter
    public static class PostRating {

        @NotBlank(message = "Cannot be left blank uuid account")
        private String uuidAccount;

        @NotBlank(message = "Cannot be left blank uuid comic")
        private String uuidComic;

        @NotBlank(message = "Cannot be left blank rating")
        private BigDecimal rating;

        @NotBlank(message = "Cannot be left blank content")
        private String content;
    }
}
