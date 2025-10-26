package com.example.projectrestfulapi.controller;

import com.example.projectrestfulapi.domain.NOSQL.Comment;
import com.example.projectrestfulapi.dto.request.Comment.CommentRequestDTO;
import com.example.projectrestfulapi.dto.response.Comment.CommentResponseDTO;
import com.example.projectrestfulapi.mapper.CommentMapper;
import com.example.projectrestfulapi.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1")
public class CommentController {
    private final CommentService commentService;
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/comments")
    public ResponseEntity<CommentResponseDTO> addComment(@Valid @RequestBody CommentRequestDTO commentRequestDTO) {
        CommentResponseDTO commentResponseDTO = CommentMapper.CommentMapper(commentService.handleComment(commentRequestDTO));
        return ResponseEntity.ok(commentResponseDTO);
    }
}
