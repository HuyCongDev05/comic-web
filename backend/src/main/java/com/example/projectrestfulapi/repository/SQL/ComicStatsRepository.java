package com.example.projectrestfulapi.repository.SQL;

import com.example.projectrestfulapi.domain.SQL.ComicStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ComicStatsRepository extends JpaRepository<ComicStats, Long> {
    @Query(value = "SELECT * FROM comic_stats WHERE comic_id IN (:comicId)", nativeQuery = true)
    List<ComicStats> findAllByComicId(@Param("comicId") List<Long> comicId);

}
