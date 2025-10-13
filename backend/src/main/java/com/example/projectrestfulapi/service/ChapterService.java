package com.example.projectrestfulapi.service;

import com.example.projectrestfulapi.domain.SQL.Chapter;
import com.example.projectrestfulapi.repository.SQL.ChapterRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChapterService {
    private final ChapterRepository chapterRepository;

    public ChapterService(ChapterRepository chapterRepository) {
        this.chapterRepository = chapterRepository;
    }

    public List<Chapter> handleGetChapterByComicId(Long comicId) {
        return chapterRepository.getChapterByComicId(comicId);
    }
}
