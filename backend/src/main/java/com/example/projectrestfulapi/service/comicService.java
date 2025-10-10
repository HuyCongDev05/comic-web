package com.example.projectrestfulapi.service;

import com.example.projectrestfulapi.domain.SQL.Comic;
import com.example.projectrestfulapi.repository.SQL.ComicRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class comicService {
    private final ComicRepository comicRepository;
    public comicService(ComicRepository comicRepository) {
        this.comicRepository = comicRepository;
    }

    public List<Comic> handleNewComic(int limit, int offset) {
        return comicRepository.getNewComic(limit, offset);
    }

    public List<Comic> handleCompletedComic(int limit, int offset) {
        return comicRepository.getCompletedComic(limit, offset);
    }
}
