package com.example.projectrestfulapi.controller;

import com.example.projectrestfulapi.domain.SQL.Comic;
import com.example.projectrestfulapi.dto.response.comic.ComicResponseDTO;
import com.example.projectrestfulapi.mapper.ComicMapper;
import com.example.projectrestfulapi.service.CategoryService;
import com.example.projectrestfulapi.service.ComicService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
public class ComicController {
    private final ComicService comicService;
    private final CategoryService categoryService;

    public ComicController(ComicService comicService, CategoryService categoryService) {
        this.comicService = comicService;
        this.categoryService = categoryService;
    }

    @GetMapping("/new-comics")
    public ResponseEntity<List<ComicResponseDTO>> getNewComics(@RequestParam(name = "page", defaultValue = "1") int pageNumber){
        int offset = (pageNumber - 1) * 20;
        List<Comic> comics = comicService.handleNewComic(offset);
        List<ComicResponseDTO> comicResponseDTOList = comics.stream()
                .map(ComicMapper::mapComicResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(comicResponseDTOList);
    }

    @GetMapping("/new-update-comics")
    public ResponseEntity<List<ComicResponseDTO>> getNewUpdateComics(@RequestParam(name = "page", defaultValue = "1") int pageNumber) {
        int offset = (pageNumber - 1) * 20;
        List<Comic> comics = comicService.handleNewUpdateComic(offset);
        List<ComicResponseDTO> comicResponseDTOList = comics.stream()
                .map(ComicMapper::mapComicResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(comicResponseDTOList);
    }

    @GetMapping("/completed-comics")
    public ResponseEntity<List<ComicResponseDTO>> getCompletedComics(@RequestParam(name = "page", defaultValue = "1") int pageNumber) {
        int offset = (pageNumber - 1) * 20;
        List<Comic> comics = comicService.handleCompletedComic(offset);
        List<ComicResponseDTO> comicResponseDTOList = comics.stream()
                .map(ComicMapper::mapComicResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(comicResponseDTOList);
    }

    @GetMapping("/categories-comics")
    public ResponseEntity<List<ComicResponseDTO>> getCategoryComics(@RequestParam("categories") String categories) {
        return null;
    }
}
