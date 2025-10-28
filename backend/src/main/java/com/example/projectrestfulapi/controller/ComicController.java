package com.example.projectrestfulapi.controller;

import com.example.projectrestfulapi.domain.SQL.Categories;
import com.example.projectrestfulapi.domain.SQL.Chapter;
import com.example.projectrestfulapi.domain.SQL.Comic;
import com.example.projectrestfulapi.domain.SQL.ComicStats;
import com.example.projectrestfulapi.dto.response.comic.CategoryResponseDTO;
import com.example.projectrestfulapi.dto.response.comic.ChapterResponseDTO;
import com.example.projectrestfulapi.dto.response.comic.ComicResponseDTO;
import com.example.projectrestfulapi.mapper.CategoriesMapper;
import com.example.projectrestfulapi.mapper.ChapterMapper;
import com.example.projectrestfulapi.mapper.ComicMapper;
import com.example.projectrestfulapi.mapper.PageMapper;
import com.example.projectrestfulapi.service.CategoriesService;
import com.example.projectrestfulapi.service.ChapterService;
import com.example.projectrestfulapi.service.ComicService;
import com.example.projectrestfulapi.service.ComicStatsService;
import com.example.projectrestfulapi.util.ComicUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
public class ComicController {
    private final ComicService comicService;
    private final CategoriesService categoryService;
    private final ChapterService chapterService;
    private final ComicStatsService comicStatsService;

    public ComicController(ComicService comicService, CategoriesService categoryService, ChapterService chapterService, ComicStatsService comicStatsService) {
        this.comicService = comicService;
        this.categoryService = categoryService;
        this.chapterService = chapterService;
        this.comicStatsService = comicStatsService;
    }

    @GetMapping("/comics/new")
    public ResponseEntity<ComicResponseDTO.PageResponseDTO> getNewComics(@RequestParam(name = "page", defaultValue = "1") int pageNumber) {
        int offset = (pageNumber - 1) * 20;
        List<Comic> comics = comicService.handleNewComic(offset);
        List<ComicStats> comicStats = comicStatsService.handleGetComicStatsByComicId(comics);
        List<ComicResponseDTO.ComicInfoResponseDTO> comicResponseDTOList = ComicUtil.mapComicsWithRatings(comics, comicStats);
        ComicResponseDTO.PageResponseDTO responseDTO = PageMapper.mapComicResponseDTOPage(comicResponseDTOList,comicService.countNewComic());
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/comics/new-update")
    public ResponseEntity<ComicResponseDTO.PageResponseDTO> getNewUpdateComics(@RequestParam(name = "page", defaultValue = "1") int pageNumber) {
        int offset = (pageNumber - 1) * 20;
        List<Comic> comics = comicService.handleNewUpdateComic(offset);
        List<ComicStats> comicStats = comicStatsService.handleGetComicStatsByComicId(comics);
        List<ComicResponseDTO.ComicInfoResponseDTO> comicResponseDTOList = ComicUtil.mapComicsWithRatings(comics, comicStats);
        ComicResponseDTO.PageResponseDTO responseDTO = PageMapper.mapComicResponseDTOPage(comicResponseDTOList,comicService.countNewUpdateAndCompletedComic());
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("/comics/completed")
    public ResponseEntity<ComicResponseDTO.PageResponseDTO> getCompletedComics(@RequestParam(name = "page", defaultValue = "1") int pageNumber) {
        int offset = (pageNumber - 1) * 20;
        List<Comic> comics = comicService.handleCompletedComic(offset);
        List<ComicStats> comicStats = comicStatsService.handleGetComicStatsByComicId(comics);
        List<ComicResponseDTO.ComicInfoResponseDTO> comicResponseDTOList = ComicUtil.mapComicsWithRatings(comics, comicStats);
        ComicResponseDTO.PageResponseDTO responseDTO = PageMapper.mapComicResponseDTOPage(comicResponseDTOList, comicService.countNewUpdateAndCompletedComic());
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("/comics/{categories}")
    public ResponseEntity<List<ComicResponseDTO.ComicInfoResponseDTO>> getCategoryComics(@PathVariable(value = "categories") String categories, @RequestParam(name = "page", defaultValue = "1") int pageNumber) {
        int offset = (pageNumber - 1) * 20;
        List<Comic> comics = comicService.handleGetComicByCategory(categories, offset);
        List<ComicStats> comicStats = comicStatsService.handleGetComicStatsByComicId(comics);
        List<ComicResponseDTO.ComicInfoResponseDTO> comicResponseDTOList = ComicUtil.mapComicsWithRatings(comics, comicStats);
        return ResponseEntity.ok().body(comicResponseDTOList);
    }

    @GetMapping("/comics/search")
    public ResponseEntity<ComicResponseDTO.PageResponseDTO> getSearchComics(@RequestParam(name = "keyword") String keyword) {
        List<Comic> comics = comicService.handleGetComicByKeyword(keyword);
        List<ComicStats> comicStats = comicStatsService.handleGetComicStatsByComicId(comics);
        List<ComicResponseDTO.ComicInfoResponseDTO> comicResponseDTOList = ComicUtil.mapComicsWithRatings(comics, comicStats);
        ComicResponseDTO.PageResponseDTO responseDTO = PageMapper.mapComicResponseDTOPage(comicResponseDTOList, comicService.countComicByKeyword(keyword));
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("/comics")
    public ResponseEntity<ComicResponseDTO.ComicDetailResponseDTO> getComicByOriginName(@RequestParam("originName") String originName) {
        Comic comic = comicService.handleFindComicByOriginName(originName);
        List<Categories> categoryList = categoryService.handleGetCategoryByComicId(comic.getId());
        List<Chapter> chapterList = chapterService.handleGetChapterByComicId(comic.getId());
        List<CategoryResponseDTO.ComicByCategory> comicByCategoryList = categoryList.stream()
                .map(CategoriesMapper::ComicByCategoryResponseDTO)
                .collect(Collectors.toList());
        List<ChapterResponseDTO.ChapterInfoResponseDTO> chapterInfoResponseDTOList = chapterList.stream()
                .map(ChapterMapper::chapterInfoResponseDTO)
                .collect(Collectors.toList());
        ComicResponseDTO.ComicDetailResponseDTO comicDetailResponseDTO = ComicMapper.mapComicDetailResponseDTO(comic, comicByCategoryList, chapterInfoResponseDTOList);
        return ResponseEntity.ok().body(comicDetailResponseDTO);
    }
}
