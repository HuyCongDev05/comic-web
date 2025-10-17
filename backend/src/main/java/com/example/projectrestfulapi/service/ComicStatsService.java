package com.example.projectrestfulapi.service;

import com.example.projectrestfulapi.domain.SQL.Comic;
import com.example.projectrestfulapi.domain.SQL.ComicStats;
import com.example.projectrestfulapi.repository.SQL.ComicStatsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ComicStatsService {
    private final ComicStatsRepository comicStatsRepository;

    public ComicStatsService(ComicStatsRepository comicStatsRepository) {
        this.comicStatsRepository = comicStatsRepository;
    }

    public List<ComicStats> handleGetComicStatsByComicId(List<Comic> comics) {
        List<Long> comicIds = comics.stream().map(Comic::getId).collect(Collectors.toList());
        return comicStatsRepository.findAllByComicId(comicIds);
    }

}
