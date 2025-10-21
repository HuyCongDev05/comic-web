package com.example.projectrestfulapi.mapper;

import com.example.projectrestfulapi.domain.SQL.Chapter;
import com.example.projectrestfulapi.domain.SQL.ImageChapter;
import com.example.projectrestfulapi.dto.response.comic.ChapterResponseDTO;

public class ChapterMapper {
    public static ChapterResponseDTO.ChapterInfoResponseDTO chapterInfoResponseDTO(Chapter chapter) {
        if (chapter == null) return null;
        ChapterResponseDTO.ChapterInfoResponseDTO chapterInfoResponseDTO = new ChapterResponseDTO.ChapterInfoResponseDTO();
        chapterInfoResponseDTO.setChapter(chapter.getChapter());
        chapterInfoResponseDTO.setChapter_url("http://localhost:8080/api/v1/chapter/" + chapter.getUuidChapter());
        chapterInfoResponseDTO.setUpdated(chapter.getTime());
        return chapterInfoResponseDTO;
    }

    public static ChapterResponseDTO.ChapterDetailResponseDTO chapterDetailResponseDTO(ImageChapter imageChapter) {
        if (imageChapter == null) return null;
        ChapterResponseDTO.ChapterDetailResponseDTO chapterDetailResponseDTO = new ChapterResponseDTO.ChapterDetailResponseDTO();
        chapterDetailResponseDTO.setImage_number(imageChapter.getImageNumber());
        chapterDetailResponseDTO.setImage_url("https://sv1.otruyencdn.com/" + imageChapter.getImageLink());
        return chapterDetailResponseDTO;
    }
}
