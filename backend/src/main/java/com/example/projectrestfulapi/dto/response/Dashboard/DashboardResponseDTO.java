package com.example.projectrestfulapi.dto.response.Dashboard;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class DashboardResponseDTO {

    @Getter
    @Setter
    public static class HomeDashboard {
        private Long totalVisits;
        private Long totalComics;
        private Long totalAccounts;
        private Long totalCompletedComics;
        private List<ViewsAndVisits> viewsAndVisits;
        private List<CategoriesRatio> categoriesRatio;
        private List<ViewsRatioComics> viewsRatioComics;

        @Getter
        public static class CategoriesRatio {
            private final String categoriesName;
            private final Number ratio;

            public CategoriesRatio(String categoryName, Number ratio) {
                this.categoriesName = categoryName;
                this.ratio = ratio.doubleValue();
            }
        }

        @Getter
        public static class ViewsRatioComics {
            private final String comicName;
            private final Long views;

            public ViewsRatioComics(String comicName, Long views) {
                this.comicName = comicName;
                this.views = views;
            }
        }

        @Getter
        public static class ViewsAndVisits {
            private final LocalDate date;
            private final Long requests;
            private final Long views;

            public ViewsAndVisits(Object date, Number requests, Number views) {
                this.date = LocalDate.parse(date.toString());
                this.requests = (requests == null) ? 0L : requests.longValue();
                this.views = (views == null) ? 0L : views.longValue();
            }
        }
    }
}

