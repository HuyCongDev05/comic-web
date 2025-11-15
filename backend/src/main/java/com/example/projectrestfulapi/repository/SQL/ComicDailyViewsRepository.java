package com.example.projectrestfulapi.repository.SQL;

import com.example.projectrestfulapi.domain.SQL.Comic;
import com.example.projectrestfulapi.domain.SQL.ComicDailyViews;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface ComicDailyViewsRepository extends JpaRepository<ComicDailyViews, Long> {
    Optional<ComicDailyViews> findByComicAndDate(Comic comic, LocalDate date);
}
