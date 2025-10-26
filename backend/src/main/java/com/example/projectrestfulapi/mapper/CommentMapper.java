package com.example.projectrestfulapi.mapper;

import com.example.projectrestfulapi.domain.NOSQL.Comment;
import com.example.projectrestfulapi.dto.response.Comment.CommentResponseDTO;

public class CommentMapper {
    public static CommentResponseDTO CommentMapper(Comment comment) {
        if (comment == null) return null;
        CommentResponseDTO commentResponseDTO = new CommentResponseDTO();
        commentResponseDTO.setComicUuid(comment.getComicUuid());
        commentResponseDTO.setAccountUuid(comment.getAccountUuid());
        commentResponseDTO.setFullName(comment.getFullName());
        commentResponseDTO.setAvatarAccount(comment.getAvatarAccount());
        commentResponseDTO.setRating(comment.getRating());
        commentResponseDTO.setMessage(comment.getMessage());
        commentResponseDTO.setTime(comment.getTime());
        return commentResponseDTO;
    }
}
