package com.example.projectrestfulapi.service;

import com.example.projectrestfulapi.domain.SQL.Comic;
import com.example.projectrestfulapi.domain.SQL.ComicDailyViews;
import com.example.projectrestfulapi.repository.SQL.ComicDailyViewsRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ComicDailyViewsService {
    private final ComicDailyViewsRepository comicDailyViewsRepository;
    public ComicDailyViewsService(ComicDailyViewsRepository comicDailyViewsRepository) {
        this.comicDailyViewsRepository = comicDailyViewsRepository;
    }

    public void handleDailyViews(Comic comic) {

        ComicDailyViews comicDailyViews = comicDailyViewsRepository.findByComicAndDate(comic, LocalDate.now())
                .orElseGet(() -> {
                    ComicDailyViews cv = new ComicDailyViews();
                    cv.setDate(LocalDate.now());
                    cv.setComic(comic);
                    cv.setViews(0L);
                    return cv;
                });
        comicDailyViews.setViews(comicDailyViews.getViews() + 1);
        comicDailyViewsRepository.save(comicDailyViews);
    }
}
