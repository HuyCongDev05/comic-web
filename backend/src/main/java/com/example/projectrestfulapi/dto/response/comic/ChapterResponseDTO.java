package com.example.projectrestfulapi.dto.response.comic;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


public class ChapterResponseDTO {
    @Getter
    @Setter
    public static class ChapterInfoResponseDTO {
        private BigDecimal chapter;
        private String chapter_url;
    }

    @Getter
    @Setter
    public static class ChapterDetailResponseDTO {
        private int image_number;
        private String image_url;
    }
}
