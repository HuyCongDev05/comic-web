package com.example.projectrestfulapi.mapper;

import com.example.projectrestfulapi.domain.SQL.Comic;
import com.example.projectrestfulapi.dto.response.comic.CategoryResponseDTO;
import com.example.projectrestfulapi.dto.response.comic.ChapterResponseDTO;
import com.example.projectrestfulapi.dto.response.comic.ComicResponseDTO;

import java.util.List;

public class ComicMapper {
    public static ComicResponseDTO.ComicInfoResponseDTO mapComicInfoResponseDTO(Comic comic) {
        if (comic == null) return null;
        ComicResponseDTO.ComicInfoResponseDTO comicResponseDTO = new ComicResponseDTO.ComicInfoResponseDTO();
        comicResponseDTO.setUuid(comic.getUuidComic());
        comicResponseDTO.setName(comic.getName());
        comicResponseDTO.setOriginName(comic.getOriginName());
        comicResponseDTO.setPoster("https://img.otruyenapi.com" + comic.getImageLink());
        comicResponseDTO.setIntro(comic.getIntro());
        comicResponseDTO.setLastChapter(comic.getLastChapter());
        comicResponseDTO.setStatus(comic.getStatus());
        comicResponseDTO.setUpdated(comic.getUpdateTime());
        return comicResponseDTO;
    }

    public static ComicResponseDTO.ComicDetailResponseDTO mapComicDetailResponseDTO(Comic comic, List<CategoryResponseDTO.ComicByCategory> categories, List<ChapterResponseDTO.ChapterInfoResponseDTO> chapters) {
        if (comic == null) return null;
        ComicResponseDTO.ComicDetailResponseDTO comicDetailResponseDTO = new ComicResponseDTO.ComicDetailResponseDTO();
        comicDetailResponseDTO.setUuid(comic.getUuidComic());
        comicDetailResponseDTO.setName(comic.getName());
        comicDetailResponseDTO.setOriginName(comic.getOriginName());
        comicDetailResponseDTO.setPoster("https://img.otruyenapi.com" + comic.getImageLink());
        comicDetailResponseDTO.setIntro(comic.getIntro());
        comicDetailResponseDTO.setLastChapter(comic.getLastChapter());
        comicDetailResponseDTO.setStatus(comic.getStatus());
        comicDetailResponseDTO.setUpdated(comic.getUpdateTime());
        comicDetailResponseDTO.setCategories(categories);
        comicDetailResponseDTO.setChapters(chapters);
        return comicDetailResponseDTO;
    }
}