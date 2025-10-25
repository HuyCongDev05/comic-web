package com.example.projectrestfulapi.controller;

import com.example.projectrestfulapi.domain.SQL.Comic;
import com.example.projectrestfulapi.domain.SQL.ComicStats;
import com.example.projectrestfulapi.dto.response.comic.ComicResponseDTO;
import com.example.projectrestfulapi.dto.request.Comic.FollowComicRequestDTO;
import com.example.projectrestfulapi.mapper.ComicMapper;
import com.example.projectrestfulapi.service.AccountFollowComicService;
import com.example.projectrestfulapi.service.ComicStatsService;
import com.example.projectrestfulapi.util.ComicUtil;
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

    @GetMapping("/follow")
    public ResponseEntity<List<ComicResponseDTO.ComicInfoResponseDTO>> getListAccountFollowComicByUuidAccount(@RequestParam("uuid") String uuid) {
        List<Comic> comics = accountFollowComicService.handleGetListAccountFollowComicByUuidAccount(uuid);
        List<ComicStats> ComicStats = comicStatsService.handleGetComicStatsByComicId(comics);
        List<ComicResponseDTO.ComicInfoResponseDTO> comicInfoResponseDTOList = ComicUtil.mapComicsWithRatings(comics, ComicStats);
        return ResponseEntity.ok().body(comicInfoResponseDTOList);
    }

    @PostMapping("/follow")
    public ResponseEntity<ComicResponseDTO.ComicInfoResponseDTO> getListComicFollowByUuidAccount(@RequestBody FollowComicRequestDTO  followComicRequestDTO) {
        Comic comic = accountFollowComicService.handleAccountFollowComic(followComicRequestDTO.getAccountUuid(), followComicRequestDTO.getComicUuid());
        ComicResponseDTO.ComicInfoResponseDTO comicInfoResponseDTO = ComicMapper.mapComicInfoResponseDTO(comic, null);
        return ResponseEntity.ok().body(comicInfoResponseDTO);
    }
    @PostMapping("/unfollow")
    public ResponseEntity<ComicResponseDTO.ComicInfoResponseDTO> unFollowComic(@RequestBody FollowComicRequestDTO followComicRequestDTO) {
        Comic comic = accountFollowComicService.handleUnfollowComic(followComicRequestDTO.getAccountUuid(), followComicRequestDTO.getComicUuid());
        ComicResponseDTO.ComicInfoResponseDTO comicInfoResponseDTO = ComicMapper.mapComicInfoResponseDTO(comic, null);
        return ResponseEntity.ok().body(comicInfoResponseDTO);
    }
}
