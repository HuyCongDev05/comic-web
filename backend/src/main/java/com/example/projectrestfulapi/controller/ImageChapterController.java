package com.example.projectrestfulapi.controller;

import com.example.projectrestfulapi.domain.SQL.ImageChapter;
import com.example.projectrestfulapi.dto.response.comic.ChapterResponseDTO;
import com.example.projectrestfulapi.mapper.ChapterMapper;
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

    public ImageChapterController(ImageChapterService imageChapterService) {
        this.imageChapterService = imageChapterService;
    }

    @GetMapping("/chapter/{uuidChapter}")
    public ResponseEntity<List<ChapterResponseDTO.ChapterDetailResponseDTO>> getChapter(@PathVariable("uuidChapter") String uuidChapter) {
        List<ImageChapter> imageChapterList = imageChapterService.handleGetImageChapterByUuidChapter(uuidChapter);
        List<ChapterResponseDTO.ChapterDetailResponseDTO> chapterDetailResponseDTOList = imageChapterList.stream()
                .map(ChapterMapper::chapterDetailResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(chapterDetailResponseDTOList);
    }
}
