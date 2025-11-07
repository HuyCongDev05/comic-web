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
import com.example.projectrestfulapi.service.*;
import com.example.projectrestfulapi.util.ComicUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private final AccountFollowComicService accountFollowComicService;

    public ComicController(ComicService comicService, CategoriesService categoryService, ChapterService chapterService, ComicStatsService comicStatsService, AccountFollowComicService accountFollowComicService) {
        this.comicService = comicService;
        this.categoryService = categoryService;
        this.chapterService = chapterService;
        this.comicStatsService = comicStatsService;
        this.accountFollowComicService = accountFollowComicService;
    }

    @GetMapping("/comics/new")
    public ResponseEntity<ComicResponseDTO.PageResponseDTO> getNewComics(@RequestParam(name = "page", defaultValue = "1") int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber - 1, 24, Sort.by(Sort.Direction.DESC, "updateTime"));
        Page<Comic> comics = comicService.handleFindByStatusAndLastChapterLessThanEqualOrderByUpdateTimeDesc("Đang cập nhật", pageable);
        List<ComicStats> comicStats = comicStatsService.handleGetComicStatsByComicId(comics);
        List<ComicResponseDTO.ComicInfoResponseDTO> comicResponseDTOList = ComicUtil.mapComicsWithRatings(comics, comicStats);
        ComicResponseDTO.PageResponseDTO responseDTO = PageMapper.mapComicResponseDTOPage(comicResponseDTOList, comics.getTotalPages(), comics.getNumberOfElements());
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/comics/new-update")
    public ResponseEntity<ComicResponseDTO.PageResponseDTO> getNewUpdateComics(@RequestParam(name = "page", defaultValue = "1") int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber - 1, 24, Sort.by(Sort.Direction.DESC, "updateTime"));
        Page<Comic> comics = comicService.handleFindByStatusOrderByUpdateTimeDesc("Đang cập nhật", pageable);
        List<ComicStats> comicStats = comicStatsService.handleGetComicStatsByComicId(comics);
        List<ComicResponseDTO.ComicInfoResponseDTO> comicResponseDTOList = ComicUtil.mapComicsWithRatings(comics, comicStats);
        ComicResponseDTO.PageResponseDTO responseDTO = PageMapper.mapComicResponseDTOPage(comicResponseDTOList, comics.getTotalPages(), comics.getNumberOfElements());
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("/comics/completed")
    public ResponseEntity<ComicResponseDTO.PageResponseDTO> getCompletedComics(@RequestParam(name = "page", defaultValue = "1") int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber - 1, 24, Sort.by(Sort.Direction.DESC, "updateTime"));
        Page<Comic> comics = comicService.handleFindByStatusOrderByUpdateTimeDesc("Đã hoàn thành", pageable);
        List<ComicStats> comicStats = comicStatsService.handleGetComicStatsByComicId(comics);
        List<ComicResponseDTO.ComicInfoResponseDTO> comicResponseDTOList = ComicUtil.mapComicsWithRatings(comics, comicStats);
        ComicResponseDTO.PageResponseDTO responseDTO = PageMapper.mapComicResponseDTOPage(comicResponseDTOList, comics.getTotalPages(), comics.getNumberOfElements());
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("/comics/{categories}")
    public ResponseEntity<ComicResponseDTO.PageResponseDTO> getCategoryComics(@PathVariable(value = "categories") String categories, @RequestParam(name = "page", defaultValue = "1") int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber - 1, 24);
        Page<Comic> comics = comicService.handeFindComicsByCategories(categories.trim().toLowerCase(), pageable);
        List<ComicStats> comicStats = comicStatsService.handleGetComicStatsByComicId(comics);
        List<ComicResponseDTO.ComicInfoResponseDTO> comicResponseDTOList = ComicUtil.mapComicsWithRatings(comics, comicStats);
        ComicResponseDTO.PageResponseDTO responseDTO = PageMapper.mapComicResponseDTOPage(comicResponseDTOList, comics.getTotalPages(), comics.getNumberOfElements());
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("/comics/search")
    public ResponseEntity<ComicResponseDTO.PageResponseDTO> getSearchComics(@RequestParam(name = "keyword") String keyword, @RequestParam(name = "page", defaultValue = "1") int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber - 1, 24);
        Page<Comic> comics = comicService.handleFindComicByKeyword(keyword.trim(), pageable);
        List<ComicStats> comicStats = comicStatsService.handleGetComicStatsByComicId(comics);
        List<ComicResponseDTO.ComicInfoResponseDTO> comicResponseDTOList = ComicUtil.mapComicsWithRatings(comics, comicStats);
        ComicResponseDTO.PageResponseDTO responseDTO = PageMapper.mapComicResponseDTOPage(comicResponseDTOList, comics.getTotalPages(), comics.getNumberOfElements());
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
        ComicResponseDTO.ComicDetailResponseDTO comicDetailResponseDTO = ComicMapper.mapComicDetailResponseDTO(comic, comicStatsService.handleGetAvgRatingByComicId(comic.getId()), accountFollowComicService.handleGetTotalFollowComic(comic.getId()), comicByCategoryList, chapterInfoResponseDTOList);
        return ResponseEntity.ok().body(comicDetailResponseDTO);
    }
}
