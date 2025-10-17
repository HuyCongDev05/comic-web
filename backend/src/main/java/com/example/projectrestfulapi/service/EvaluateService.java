package com.example.projectrestfulapi.service;

import com.example.projectrestfulapi.domain.SQL.Account;
import com.example.projectrestfulapi.domain.SQL.Comic;
import com.example.projectrestfulapi.domain.SQL.Evaluate;
import com.example.projectrestfulapi.dto.request.Evaluate.EvaluateRequestDTO;
import com.example.projectrestfulapi.dto.response.Evaluate.EvaluateResponseDTO;
import com.example.projectrestfulapi.mapper.EvaluateMapper;
import com.example.projectrestfulapi.repository.SQL.AccountRepository;
import com.example.projectrestfulapi.repository.SQL.ComicRepository;
import com.example.projectrestfulapi.repository.SQL.EvaluateRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EvaluateService {
    private final EvaluateRepository evaluateRepository;
    private final AccountRepository accountRepository;
    private final ComicRepository comicRepository;

    public EvaluateService(EvaluateRepository evaluateRepository, AccountRepository accountRepository, ComicRepository comicRepository) {
        this.evaluateRepository = evaluateRepository;
        this.accountRepository = accountRepository;
        this.comicRepository = comicRepository;
    }

    public Evaluate handleGetRating(String uuidAccount, String uuidComic) {
        Long accountId = accountRepository.findAccountIdByUuid(uuidAccount);
        Long comicId = comicRepository.findComicIdByUuidComic(uuidComic);
        return evaluateRepository.findByComicIdAndAccountId(comicId, accountId);
    }

    @Transactional
    public EvaluateResponseDTO HandleCreateEvaluate(EvaluateRequestDTO.PostRating postRating) {
        Account accountId = accountRepository.findByUuid(postRating.getUuidAccount())
                .orElseThrow(() -> new IllegalArgumentException("account not found"));
        Comic comicId = comicRepository.findComicByUuidComic(postRating.getUuidComic())
                .orElseThrow(() -> new IllegalArgumentException("comic not found"));
        Evaluate evaluate = new Evaluate();
        evaluate.setAccountId(accountId);
        evaluate.setComicId(comicId);
        evaluate.setRating(postRating.getRating());
        evaluate.setContent(postRating.getContent());
        evaluateRepository.save(evaluate);
        return EvaluateMapper.evaluateMapper(evaluate);
    }
}
