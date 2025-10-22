package com.example.projectrestfulapi.mapper;

import com.example.projectrestfulapi.domain.SQL.Chapter;
import com.example.projectrestfulapi.domain.SQL.ImageChapter;
import com.example.projectrestfulapi.dto.response.comic.ChapterResponseDTO;

import java.math.BigDecimal;
import java.util.List;

public class ChapterMapper {
    public static ChapterResponseDTO.ChapterInfoResponseDTO chapterInfoResponseDTO(Chapter chapter) {
        if (chapter == null) return null;
        ChapterResponseDTO.ChapterInfoResponseDTO chapterInfoResponseDTO = new ChapterResponseDTO.ChapterInfoResponseDTO();
        chapterInfoResponseDTO.setChapter(chapter.getChapter());
        chapterInfoResponseDTO.setChapter_uuid(chapter.getUuidChapter());
        chapterInfoResponseDTO.setUpdated(chapter.getTime());
        return chapterInfoResponseDTO;
    }

    public static ChapterResponseDTO.ChapterDetailResponseDTO chapterDetailResponseDTO(String originName, BigDecimal chapterNumber, Long chapterTotal, List<ChapterResponseDTO.ImageChapterResponseDTO> imageChapter) {
        if (imageChapter == null) return null;
        ChapterResponseDTO.ChapterDetailResponseDTO chapterDetailResponseDTO = new ChapterResponseDTO.ChapterDetailResponseDTO();
        chapterDetailResponseDTO.setOrigin_name(originName);
        chapterDetailResponseDTO.setChapter_number(chapterNumber);
        chapterDetailResponseDTO.setTotal_chapters(chapterTotal);
        chapterDetailResponseDTO.setChapters(imageChapter);
        return chapterDetailResponseDTO;
    }

    public static ChapterResponseDTO.ImageChapterResponseDTO imageChapterResponseDTO(ImageChapter imageChapter) {
        if (imageChapter == null) return null;
        ChapterResponseDTO.ImageChapterResponseDTO imageChapterResponseDTO = new ChapterResponseDTO.ImageChapterResponseDTO();
        imageChapterResponseDTO.setImage_number(imageChapter.getImageNumber());
        imageChapterResponseDTO.setImage_url("https://sv1.otruyencdn.com/" + imageChapter.getImageLink());
        return  imageChapterResponseDTO;
    }
}
