package com.example.projectrestfulapi.service;

import com.example.projectrestfulapi.domain.NOSQL.Comment;
import com.example.projectrestfulapi.domain.SQL.Account;
import com.example.projectrestfulapi.domain.SQL.User;
import com.example.projectrestfulapi.dto.request.Comment.CommentRequestDTO;
import com.example.projectrestfulapi.exception.InvalidException;
import com.example.projectrestfulapi.exception.NumberError;
import com.example.projectrestfulapi.repository.NOSQL.CommentRepository;
import com.example.projectrestfulapi.repository.SQL.AccountRepository;
import com.example.projectrestfulapi.repository.SQL.ComicRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final ComicRepository comicRepository;
    private final AccountRepository accountRepository;
    public CommentService(CommentRepository commentRepository, ComicRepository comicRepository, AccountRepository accountRepository) {
        this.commentRepository = commentRepository;
        this.comicRepository = comicRepository;
        this.accountRepository = accountRepository;
    }

    public Comment handleComment(@RequestBody CommentRequestDTO commentRequestDTO) {
        if (!accountRepository.existsByUuid(commentRequestDTO.getAccountUuid())) {
            throw new InvalidException(NumberError.ACCOUNT_NOT_FOUND.getMessage(), NumberError.ACCOUNT_NOT_FOUND);
        } else if (!comicRepository.existsByUuidComic(commentRequestDTO.getComicUuid())) {
            throw new InvalidException(NumberError.COMIC_NOT_FOUND.getMessage(), NumberError.COMIC_NOT_FOUND);
        }
        Optional<Account> account = accountRepository.findByUuid(commentRequestDTO.getAccountUuid());
        User user = account.get().getUser();
        Comment comment = new Comment();
        comment.setComicUuid(commentRequestDTO.getComicUuid());
        comment.setAccountUuid(commentRequestDTO.getAccountUuid());
        comment.setFullName(user.getFirstName() + " " + user.getLastName());
        comment.setAvatarAccount(account.get().getAvatar());
        comment.setRating(commentRequestDTO.getRating());
        comment.setMessage(commentRequestDTO.getMessage());
        return commentRepository.save(comment);
    }
}
