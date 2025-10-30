package com.example.projectrestfulapi.controller;

import com.example.projectrestfulapi.dto.request.Comment.CommentRequestDTO;
import com.example.projectrestfulapi.dto.response.Comment.CommentResponseDTO;
import com.example.projectrestfulapi.mapper.CommentMapper;
import com.example.projectrestfulapi.service.ComicService;
import com.example.projectrestfulapi.service.ComicStatsService;
import com.example.projectrestfulapi.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/v1")
public class CommentController {
    private final CommentService commentService;
    private final ComicService comicService;
    private final ComicStatsService comicStatsService;
    public CommentController(CommentService commentService, ComicService comicService, ComicStatsService comicStatsService) {
        this.commentService = commentService;
        this.comicService = comicService;
        this.comicStatsService = comicStatsService;
    }

    @PostMapping("/comics/{comicUuid}/comments")
    public ResponseEntity<CommentResponseDTO> addComment(@PathVariable(value = "comicUuid") String comicUuid,@Valid @RequestBody CommentRequestDTO commentRequestDTO) {
        comicStatsService.handleRating(commentRequestDTO.getRating(),comicService.handleGetComicIdByComicUuid(comicUuid));
        CommentResponseDTO commentResponseDTO = CommentMapper.CommentMapper(commentService.handleComment(comicUuid,commentRequestDTO));
        return ResponseEntity.ok(commentResponseDTO);
    }

    @GetMapping("/comics/{comicUuid}/comments")
    public ResponseEntity<List<CommentResponseDTO>> getAllCommentsByComicUuid(@PathVariable(value = "comicUuid") String comicUuid) {
        List<CommentResponseDTO> commentResponseDTOList = commentService.handleGetCommentsByComicUuid(comicUuid).stream()
                .map(CommentMapper::CommentMapper)
                .collect(Collectors.toList());
        return ResponseEntity.ok(commentResponseDTOList);
    }
}
