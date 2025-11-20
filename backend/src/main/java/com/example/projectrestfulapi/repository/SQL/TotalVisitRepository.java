package com.example.projectrestfulapi.repository.SQL;

import com.example.projectrestfulapi.domain.SQL.TotalVisit;
import com.example.projectrestfulapi.dto.response.Dashboard.DashboardResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TotalVisitRepository extends JpaRepository<TotalVisit, Long> {
    Optional<TotalVisit> findByDate(LocalDate date);

    @Query(value = """
                SELECT 
                    CAST(tv.date AS DATE) AS date,
                    tv.total_request AS requests,
                    SUM(cdv.views) AS views
                FROM total_visit tv
                LEFT JOIN comic.comic_daily_views cdv 
                    ON cdv.date = tv.date
                WHERE MONTH(tv.date) = MONTH(CURRENT_DATE())
                  AND YEAR(tv.date) = YEAR(CURRENT_DATE())
                GROUP BY tv.date, tv.total_request
                ORDER BY date ASC
            """, nativeQuery = true)
    List<DashboardResponseDTO.HomeDashboard.ViewsAndVisits> findViewsAndVisits();

    @Query(value = " SELECT sum(total_request) FROM comic.total_visit; ", nativeQuery = true)
    Long totalVisit();
}
