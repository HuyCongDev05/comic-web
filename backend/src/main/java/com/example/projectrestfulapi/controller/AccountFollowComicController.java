package com.example.projectrestfulapi.controller;

import com.example.projectrestfulapi.domain.SQL.Comic;
import com.example.projectrestfulapi.dto.response.comic.ComicResponseDTO;
import com.example.projectrestfulapi.mapper.ComicMapper;
import com.example.projectrestfulapi.service.AccountFollowComicService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/v1")
public class AccountFollowComicController {
    private final AccountFollowComicService accountFollowComicService;

    public AccountFollowComicController(AccountFollowComicService accountFollowComicService) {
        this.accountFollowComicService = accountFollowComicService;
    }

    @GetMapping("/follow")
    public ResponseEntity<List<ComicResponseDTO.ComicInfoResponseDTO>> handleFindListAccountFollowComicByAccountId(@RequestParam("uuid") String uuid) {
        List<Comic> comicList = accountFollowComicService.handleGetListAccountFollowComicByAccountId(uuid);
        List<ComicResponseDTO.ComicInfoResponseDTO> comicInfoResponseDTOList = comicList.stream()
                .map(ComicMapper::mapComicInfoResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(comicInfoResponseDTOList);
    }
}
