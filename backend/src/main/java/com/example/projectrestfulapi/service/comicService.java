package com.example.projectrestfulapi.service;

import com.example.projectrestfulapi.domain.SQL.Comic;
import com.example.projectrestfulapi.exception.InvalidException;
import com.example.projectrestfulapi.exception.NumberError;
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
        return comicRepository.getComicByOriginName(OriginName).orElseThrow(() -> new InvalidException(NumberError.COMIC_NOT_FOUND.getMessage(), NumberError.COMIC_NOT_FOUND));
    }

    public List<Comic> handleGetComicByKeyword(String keyword, int offset) {
        return comicRepository.findComicByKeyword(keyword, offset);
    }

    public List<Comic> handleGetComicByCategory(String category, int offset) {
        return comicRepository.getComicByCategory(category, offset);
    }

    public Long countComicByKeyword(String keyword) {
        long totalElements = comicRepository.countComicByKeyword(keyword);
        long pageSize = 21L;
        return (long) Math.ceil((double) totalElements / pageSize);
    }

    public long countNewComic() {
        long totalElements = comicRepository.countNewComics();
        long pageSize = 21L;
        return (long) Math.ceil((double) totalElements / pageSize);
    }

    public Long countNewUpdateComics() {
        long totalElements = comicRepository.countNewUpdateComics();
        long pageSize = 21L;
        return (long) Math.ceil((double) totalElements / pageSize);
    }

    public Long countCompletedComics() {
        long totalElements = comicRepository.countCompletedComics();
        long pageSize = 21L;
        return (long) Math.ceil((double) totalElements / pageSize);
    }

    public long handleGetComicIdByComicUuid(String comicUuid) {
        Comic comic = comicRepository.findComicByUuidComic(comicUuid)
                .orElseThrow(() -> new InvalidException(
                        NumberError.COMIC_NOT_FOUND.getMessage(),
                        NumberError.COMIC_NOT_FOUND
                ));
        return comic.getId();
    }
}
