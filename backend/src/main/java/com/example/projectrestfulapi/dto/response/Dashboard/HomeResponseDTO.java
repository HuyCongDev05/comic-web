package com.example.projectrestfulapi.dto.response.Dashboard;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class HomeResponseDTO {
    private Long totalViews;
    private Long totalComics;
    private Long totalUsers;
    private Long totalCompletedComics;
    private List<CategoriesRatio> categoriesRatio;
    private List<ViewsRatioComics> viewsRatioComics;

    public static class CategoriesRatio {
        private String categoryName;
        private Double ratio;
    }

    private static class ViewsRatioComics {
        private String comicName;
        private Long views;
    }
}
