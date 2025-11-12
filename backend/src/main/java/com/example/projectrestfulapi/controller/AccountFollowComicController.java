package com.example.projectrestfulapi.controller;

import com.example.projectrestfulapi.domain.SQL.Comic;
import com.example.projectrestfulapi.domain.SQL.ComicStats;
import com.example.projectrestfulapi.dto.request.Comic.FollowAndHistoryComicRequestDTO;
import com.example.projectrestfulapi.dto.response.comic.ComicResponseDTO;
import com.example.projectrestfulapi.mapper.ComicMapper;
import com.example.projectrestfulapi.service.AccountFollowComicService;
import com.example.projectrestfulapi.service.ComicStatsService;
import com.example.projectrestfulapi.util.ComicUtil;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/v1")
public class AccountFollowComicController {
    private final AccountFollowComicService accountFollowComicService;
    private final ComicStatsService comicStatsService;

    public AccountFollowComicController(AccountFollowComicService accountFollowComicService, ComicStatsService comicStatsService) {
        this.accountFollowComicService = accountFollowComicService;
        this.comicStatsService = comicStatsService;
    }

    @GetMapping("comics/follows")
    public ResponseEntity<List<ComicResponseDTO.ComicInfoResponseDTO>> getListComicFollowByAccountUuid(@RequestParam("account_uuid") String accountUuid, @RequestParam(value = "page", defaultValue = "1") int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber - 1, 24);
        Page<Comic> comics = accountFollowComicService.handleGetListAccountFollowComicByUuidAccount(accountUuid, pageable);
        List<ComicStats> ComicStats = comicStatsService.handleGetComicStatsByComicId(comics);
        List<ComicResponseDTO.ComicInfoResponseDTO> comicInfoResponseDTOList = ComicUtil.mapComicsWithRatings(comics, ComicStats);
        return ResponseEntity.ok().body(comicInfoResponseDTOList);
    }

    @PostMapping("comics/follows")
    public ResponseEntity<ComicResponseDTO.ComicInfoResponseDTO> followComic(@Valid @RequestBody FollowAndHistoryComicRequestDTO followComicRequestDTO) {
        Comic comic = accountFollowComicService.handleAccountFollowComic(followComicRequestDTO.getAccountUuid(), followComicRequestDTO.getComicUuid());
        ComicResponseDTO.ComicInfoResponseDTO comicInfoResponseDTO = ComicMapper.mapComicInfoResponseDTO(comic, null);
        return ResponseEntity.ok().body(comicInfoResponseDTO);
    }

    @PostMapping("comics/unfollows")
    public ResponseEntity<ComicResponseDTO.ComicInfoResponseDTO> unFollowComic(@Valid @RequestBody FollowAndHistoryComicRequestDTO followComicRequestDTO) {
        Comic comic = accountFollowComicService.handleUnfollowComic(followComicRequestDTO.getAccountUuid(), followComicRequestDTO.getComicUuid());
        ComicResponseDTO.ComicInfoResponseDTO comicInfoResponseDTO = ComicMapper.mapComicInfoResponseDTO(comic, null);
        return ResponseEntity.ok().body(comicInfoResponseDTO);
    }
}
