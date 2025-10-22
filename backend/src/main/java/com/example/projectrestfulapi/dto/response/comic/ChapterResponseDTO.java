package com.example.projectrestfulapi.dto.response.comic;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;


public class ChapterResponseDTO {
    @Getter
    @Setter
    public static class ChapterInfoResponseDTO {
        private BigDecimal chapter;
        private String chapter_uuid;
        private Instant updated;
    }

    @Getter
    @Setter
    public static class ImageChapterResponseDTO {
        private int image_number;
        private String image_url;
    }

    @Getter
    @Setter
    public static class ChapterDetailResponseDTO {
        private String origin_name;
        private BigDecimal chapter_number;
        private Long total_chapters;
        List<ImageChapterResponseDTO> chapters;
    }
}
