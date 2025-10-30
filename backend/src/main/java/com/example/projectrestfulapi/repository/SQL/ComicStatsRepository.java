package com.example.projectrestfulapi.repository.SQL;

import com.example.projectrestfulapi.domain.SQL.ComicStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ComicStatsRepository extends JpaRepository<ComicStats, Long> {
    @Query(value = "SELECT * FROM comic_stats WHERE comic_id IN (:comicId)", nativeQuery = true)
    List<ComicStats> findAllByComicId(@Param("comicId") List<Long> comicId);

    @Modifying
    @Query(value = """
    UPDATE comic_stats
    SET avg_rating = ROUND(((avg_rating * total_rating) + :rating) / (total_rating + 1), 1),
        total_rating = total_rating + 1
    WHERE comic_id = :comic_id
    """, nativeQuery = true)
    void updateRating(@Param("rating") BigDecimal rating, @Param("comic_id") Long comicId);

    @Query(value = "SELECT avg_rating FROM comic_stats where comic_id = :comicId", nativeQuery = true)
    BigDecimal getAvgRatingByComicId(@Param("comicId") Long comicId);
}
