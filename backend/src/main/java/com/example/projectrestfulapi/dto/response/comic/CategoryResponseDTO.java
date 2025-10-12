package com.example.projectrestfulapi.dto.response.comic;

import lombok.Getter;
import lombok.Setter;

public class CategoryResponseDTO {
    @Getter
    @Setter
    public static class comicByCategory{
        private String originName;
        private String categoryName;
    }

    @Getter
    @Setter
    public static class categoryByCategory{
        private String originName;
        private String name;
        private String detail;
    }
}
