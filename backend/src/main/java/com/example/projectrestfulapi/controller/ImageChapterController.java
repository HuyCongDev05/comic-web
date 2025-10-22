package com.example.projectrestfulapi.controller;

import com.example.projectrestfulapi.domain.SQL.ImageChapter;
import com.example.projectrestfulapi.dto.response.comic.ChapterResponseDTO;
import com.example.projectrestfulapi.mapper.ChapterMapper;
import com.example.projectrestfulapi.service.ChapterService;
import com.example.projectrestfulapi.service.ImageChapterService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/v1")
public class ImageChapterController {
    private final ImageChapterService imageChapterService;
    private final ChapterService  chapterService;

    public ImageChapterController(ImageChapterService imageChapterService, ChapterService chapterService) {
        this.imageChapterService = imageChapterService;
        this.chapterService = chapterService;
    }

    @GetMapping("/chapter/{uuidChapter}")
    public ResponseEntity<ChapterResponseDTO.ChapterDetailResponseDTO> getChapter(@PathVariable("uuidChapter") String uuidChapter) {
        List<ImageChapter> imageChapterList = imageChapterService.handleGetImageChapterByUuidChapter(uuidChapter);
        List<ChapterResponseDTO.ImageChapterResponseDTO> imageChapterResponseDTOS = imageChapterList.stream()
                .map(ChapterMapper::imageChapterResponseDTO)
                .collect(Collectors.toList());
        ChapterResponseDTO.ChapterDetailResponseDTO chapterDetailResponseDTOList = ChapterMapper.chapterDetailResponseDTO(chapterService.handleGetOriginNameByUuidChapter(uuidChapter), chapterService.handleGetChapterByUuidChapter(uuidChapter), chapterService.handleGetTotalChapterByUuidChapter(uuidChapter), imageChapterResponseDTOS);
        return ResponseEntity.ok().body(chapterDetailResponseDTOList);
    }
}
