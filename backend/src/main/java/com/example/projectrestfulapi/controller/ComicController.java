package com.example.projectrestfulapi.controller;

import com.example.projectrestfulapi.domain.SQL.Category;
import com.example.projectrestfulapi.domain.SQL.Chapter;
import com.example.projectrestfulapi.domain.SQL.Comic;
import com.example.projectrestfulapi.domain.SQL.ComicStats;
import com.example.projectrestfulapi.dto.response.comic.CategoryResponseDTO;
import com.example.projectrestfulapi.dto.response.comic.ChapterResponseDTO;
import com.example.projectrestfulapi.dto.response.comic.ComicResponseDTO;
import com.example.projectrestfulapi.mapper.CategoryMapper;
import com.example.projectrestfulapi.mapper.ChapterMapper;
import com.example.projectrestfulapi.mapper.ComicMapper;
import com.example.projectrestfulapi.service.CategoryService;
import com.example.projectrestfulapi.service.ChapterService;
import com.example.projectrestfulapi.service.ComicService;
import com.example.projectrestfulapi.service.ComicStatsService;
import com.example.projectrestfulapi.util.ComicUtil;
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
    private final ChapterService chapterService;
    private final ComicStatsService comicStatsService;

    public ComicController(ComicService comicService, CategoryService categoryService, ChapterService chapterService, ComicStatsService comicStatsService) {
        this.comicService = comicService;
        this.categoryService = categoryService;
        this.chapterService = chapterService;
        this.comicStatsService = comicStatsService;
    }

    @GetMapping("/new-comics")
    public ResponseEntity<List<ComicResponseDTO.ComicInfoResponseDTO>> getNewComics(@RequestParam(name = "page", defaultValue = "1") int pageNumber) {
        int offset = (pageNumber - 1) * 20;
        List<Comic> comics = comicService.handleNewComic(offset);
        List<ComicStats> comicStats = comicStatsService.handleGetComicStatsByComicId(comics);
        List<ComicResponseDTO.ComicInfoResponseDTO> comicResponseDTOList = ComicUtil.mapComicsWithRatings(comics, comicStats);
        return ResponseEntity.ok(comicResponseDTOList);
    }

    @GetMapping("/new-update-comics")
    public ResponseEntity<List<ComicResponseDTO.ComicInfoResponseDTO>> getNewUpdateComics(@RequestParam(name = "page", defaultValue = "1") int pageNumber) {
        int offset = (pageNumber - 1) * 20;
        List<Comic> comics = comicService.handleNewUpdateComic(offset);
        List<ComicStats> comicStats = comicStatsService.handleGetComicStatsByComicId(comics);
        List<ComicResponseDTO.ComicInfoResponseDTO> comicResponseDTOList = ComicUtil.mapComicsWithRatings(comics, comicStats);
        return ResponseEntity.ok().body(comicResponseDTOList);
    }

    @GetMapping("/completed-comics")
    public ResponseEntity<List<ComicResponseDTO.ComicInfoResponseDTO>> getCompletedComics(@RequestParam(name = "page", defaultValue = "1") int pageNumber) {
        int offset = (pageNumber - 1) * 20;
        List<Comic> comics = comicService.handleCompletedComic(offset);
        List<ComicStats> comicStats = comicStatsService.handleGetComicStatsByComicId(comics);
        List<ComicResponseDTO.ComicInfoResponseDTO> comicResponseDTOList = ComicUtil.mapComicsWithRatings(comics, comicStats);
        return ResponseEntity.ok().body(comicResponseDTOList);
    }

    @GetMapping("/categories-comics")
    public ResponseEntity<List<ComicResponseDTO.ComicInfoResponseDTO>> getCategoryComics(@RequestParam("categories") String categories, @RequestParam(name = "page", defaultValue = "1") int pageNumber) {
        int offset = (pageNumber - 1) * 20;
        List<Comic> comics = comicService.handleGetComicByCategory(categories, offset);
        List<ComicStats> comicStats = comicStatsService.handleGetComicStatsByComicId(comics);
        List<ComicResponseDTO.ComicInfoResponseDTO> comicResponseDTOList = ComicUtil.mapComicsWithRatings(comics, comicStats);
        return ResponseEntity.ok().body(comicResponseDTOList);
    }

    @GetMapping("/search-comics")
    public ResponseEntity<List<ComicResponseDTO.ComicInfoResponseDTO>> getSearchComics(@RequestParam(name = "keyword") String keyword) {
        List<Comic> comics = comicService.handleGetComicByKeyword(keyword);
        List<ComicStats> comicStats = comicStatsService.handleGetComicStatsByComicId(comics);
        List<ComicResponseDTO.ComicInfoResponseDTO> comicResponseDTOList = ComicUtil.mapComicsWithRatings(comics, comicStats);
        return ResponseEntity.ok().body(comicResponseDTOList);
    }

    @GetMapping("/name-comics")
    public ResponseEntity<ComicResponseDTO.ComicDetailResponseDTO> getComicByOriginName(@RequestParam("originName") String originName) {
        Comic comic = comicService.handleFindComicByOriginName(originName);
        List<Category> categoryList = categoryService.handleGetCategoryByComicId(comic.getId());
        List<Chapter> chapterList = chapterService.handleGetChapterByComicId(comic.getId());
        List<CategoryResponseDTO.ComicByCategory> comicByCategoryList = categoryList.stream()
                .map(CategoryMapper::ComicByCategoryResponseDTO)
                .collect(Collectors.toList());
        List<ChapterResponseDTO.ChapterInfoResponseDTO> chapterInfoResponseDTOList = chapterList.stream()
                .map(ChapterMapper::chapterInfoResponseDTO)
                .collect(Collectors.toList());
        ComicResponseDTO.ComicDetailResponseDTO comicDetailResponseDTO = ComicMapper.mapComicDetailResponseDTO(comic, comicByCategoryList, chapterInfoResponseDTOList);
        return ResponseEntity.ok().body(comicDetailResponseDTO);
    }
}
