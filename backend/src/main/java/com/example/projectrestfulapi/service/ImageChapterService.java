package com.example.projectrestfulapi.service;

import com.example.projectrestfulapi.domain.SQL.ImageChapter;
import com.example.projectrestfulapi.repository.SQL.ImageChapterRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImageChapterService {
    private final ImageChapterRepository imageChapterRepository;

    public ImageChapterService(ImageChapterRepository imageChapterRepository) {
        this.imageChapterRepository = imageChapterRepository;
    }

    public List<ImageChapter> handleGetImageChapterByUuidChapter(String uuidChapter) {
        return imageChapterRepository.getImageChapterByUuidChapter(uuidChapter);
    }
}
