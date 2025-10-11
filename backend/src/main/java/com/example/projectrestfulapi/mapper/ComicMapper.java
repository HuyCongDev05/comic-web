package com.example.projectrestfulapi.mapper;

import com.example.projectrestfulapi.domain.SQL.Comic;
import com.example.projectrestfulapi.dto.response.comic.ComicResponseDTO;

public class ComicMapper {
    public static ComicResponseDTO mapComicResponseDTO(Comic comic) {
        if (comic == null) return null;
        ComicResponseDTO comicResponseDTO = new ComicResponseDTO();
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
}