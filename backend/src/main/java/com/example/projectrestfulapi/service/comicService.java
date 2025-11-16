package com.example.projectrestfulapi.service;

import com.example.projectrestfulapi.domain.SQL.Comic;
import com.example.projectrestfulapi.exception.InvalidException;
import com.example.projectrestfulapi.exception.NumberError;
import com.example.projectrestfulapi.repository.SQL.ComicRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ComicService {
    private final ComicRepository comicRepository;

    public ComicService(ComicRepository comicRepository) {
        this.comicRepository = comicRepository;
    }

    public Page<Comic> handleFindByStatusOrderByUpdateTimeDesc(String status, Pageable pageable) {
        return comicRepository.findByStatusOrderByUpdateTimeDesc(status, pageable);
    }

    public Page<Comic> handleFindByStatusAndLastChapterLessThanEqualOrderByUpdateTimeDesc(String status, Pageable pageable) {
        return comicRepository.findByStatusAndLastChapterLessThanEqualOrderByUpdateTimeDesc(status, BigDecimal.valueOf(15), pageable);
    }

    public Page<Comic> handeFindComicsByCategories(String categories, Pageable pageable) {
        return comicRepository.findComicByCategories(categories, pageable);
    }

    public Comic handleFindComicByOriginName(String OriginName) {
        return comicRepository.getComicByOriginName(OriginName).orElseThrow(() -> new InvalidException(NumberError.COMIC_NOT_FOUND.getMessage(), NumberError.COMIC_NOT_FOUND));
    }

    public Page<Comic> handleFindComicByKeyword(String keyword, Pageable pageable) {
        return comicRepository.findComicByKeyword(keyword, pageable);
    }

    public long handleFindComicIdByComicUuid(String comicUuid) {
        Comic comic = comicRepository.findComicByUuidComic(comicUuid)
                .orElseThrow(() -> new InvalidException(
                        NumberError.COMIC_NOT_FOUND.getMessage(),
                        NumberError.COMIC_NOT_FOUND
                ));
        return comic.getId();
    }

    public Page<Comic> handleFindComicByComicUuids(List<String> comicUuids, Pageable pageable) {
        List<Comic> comics = comicRepository.findByUuidComicIn(comicUuids);
        return new PageImpl<>(comics, pageable, comics.size());
    }

    public Long handleGetTotalComics() {
        return comicRepository.count();
    }

    public Long handleGetTotalCompletedComics() {
        return comicRepository.getCountComicByStatus("Đã hoàn thành");
    }
}
