package com.example.projectrestfulapi.repository.SQL;

import com.example.projectrestfulapi.domain.SQL.Comic;
import com.example.projectrestfulapi.domain.SQL.ComicDailyViews;
import com.example.projectrestfulapi.dto.response.Dashboard.DashboardResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ComicDailyViewsRepository extends JpaRepository<ComicDailyViews, Long> {
    Optional<ComicDailyViews> findByComicAndDate(Comic comic, LocalDate date);

    @Query(value = """
            SELECT comic.name, comic_daily_views.views FROM comic.comic_daily_views
            inner join comic on comic.id = comic_daily_views.comic_id
            order by views desc
            limit 5;""", nativeQuery = true)
    List<DashboardResponseDTO.HomeDashboard.ViewsRatioComics> findViewsRatioComics();
}
