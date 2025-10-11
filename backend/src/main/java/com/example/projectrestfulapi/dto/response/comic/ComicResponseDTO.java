package com.example.projectrestfulapi.dto.response.comic;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
public class ComicResponseDTO {
    private String uuid;
    private String name;
    private String originName;
    private String poster;
    private String intro;
    private BigDecimal lastChapter;
    private String status;
    private Instant updated;
}
