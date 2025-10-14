package com.example.projectrestfulapi.service;

import com.example.projectrestfulapi.domain.SQL.Comic;
import com.example.projectrestfulapi.repository.SQL.ComicRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComicService {
    private final ComicRepository comicRepository;

    public ComicService(ComicRepository comicRepository) {
        this.comicRepository = comicRepository;
    }

    public List<Comic> handleNewComic(int offset) {
        return comicRepository.getNewComic(offset);
    }

    public List<Comic> handleNewUpdateComic(int offset) {
        return comicRepository.getNewUpdateComic(offset);
    }

    public List<Comic> handleCompletedComic(int offset) {
        return comicRepository.getCompletedComic(offset);
    }

    public Comic handleFindComicByOriginName(String OriginName) {
        return comicRepository.getComicByOriginName(OriginName).orElseThrow(null);
    }

    public List<Comic> handleFindComicByKeyword(String keyword) {
        return comicRepository.findComicByKeyword(keyword);
    }

    public List<Comic> handleFindComicByCategory(String category, int offset) {
        return comicRepository.getComicByCategory(category, offset);
    }
}
