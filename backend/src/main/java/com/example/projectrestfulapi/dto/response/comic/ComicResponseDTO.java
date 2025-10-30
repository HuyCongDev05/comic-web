package com.example.projectrestfulapi.dto.response.comic;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public class ComicResponseDTO {
    @Getter
    @Setter
    public static class ComicInfoResponseDTO {

        private String uuid;
        private String name;
        private String originName;
        private String poster;
        private String intro;
        private BigDecimal lastChapter;
        private String status;
        private BigDecimal rating;
        private Instant updated;
    }

    @Getter
    @Setter
    public static class ComicDetailResponseDTO {
        private String uuid;
        private String name;
        private String originName;
        private String poster;
        private String intro;
        private BigDecimal rating;
        private Long totalUserFollow;
        private BigDecimal lastChapter;
        private String status;
        private Instant updated;
        List<CategoryResponseDTO.ComicByCategory> categories;
        List<ChapterResponseDTO.ChapterInfoResponseDTO> chapters;
    }

    @Getter
    @Setter
    public static class PageResponseDTO {
        private List<ComicInfoResponseDTO> content;
        private Long totalPages;
    }
}
