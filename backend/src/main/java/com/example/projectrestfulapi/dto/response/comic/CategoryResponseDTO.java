package com.example.projectrestfulapi.dto.response.comic;

import lombok.Getter;
import lombok.Setter;

public class CategoryResponseDTO {
    @Getter
    @Setter
    public static class ComicByCategory {
        private String originName;
        private String categoriesName;
    }

    @Getter
    @Setter
    public static class DetailCategory {
        private String originName;
        private String name;
        private String detail;
    }
}
