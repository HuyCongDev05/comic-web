package com.example.projectrestfulapi.service;

import com.example.projectrestfulapi.domain.SQL.Account;
import com.example.projectrestfulapi.domain.SQL.AccountFollowComic;
import com.example.projectrestfulapi.domain.SQL.Comic;
import com.example.projectrestfulapi.exception.InvalidException;
import com.example.projectrestfulapi.exception.NumberError;
import com.example.projectrestfulapi.repository.SQL.AccountFollowComicRepository;
import com.example.projectrestfulapi.repository.SQL.AccountRepository;
import com.example.projectrestfulapi.repository.SQL.ComicRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountFollowComicService {
    private final AccountFollowComicRepository accountFollowComicRepository;
    private final AccountRepository accountRepository;
    private final ComicRepository comicRepository;

    public AccountFollowComicService(AccountFollowComicRepository accountFollowComicRepository, AccountRepository accountRepository, ComicRepository comicRepository) {
        this.accountFollowComicRepository = accountFollowComicRepository;
        this.accountRepository = accountRepository;
        this.comicRepository = comicRepository;
    }

    public List<Comic> handleGetListAccountFollowComicByUuidAccount(String uuid) {
        Long accountId = accountRepository.findAccountIdByUuid(uuid);
        List<AccountFollowComic> accountFollowComics = accountFollowComicRepository.findByAccountId(accountId);
        return accountFollowComics.stream().map(AccountFollowComic::getComic).collect(Collectors.toList());
    }

    @Transactional
    public Comic handleAccountFollowComic(String accountUuid, String comicUuid) {
        Account account =  accountRepository.findByUuid(accountUuid).orElseThrow(() -> new InvalidException(NumberError.ACCOUNT_NOT_FOUND.getMessage(), NumberError.ACCOUNT_NOT_FOUND));
        Comic comic = comicRepository.findComicByUuidComic(comicUuid).orElseThrow(() -> new InvalidException(NumberError.COMIC_NOT_FOUND.getMessage(), NumberError.COMIC_NOT_FOUND));
        if (!accountFollowComicRepository.existsByAccountIdAndComicId(account.getId(), comic.getId())) {
            AccountFollowComic accountFollowComic = new AccountFollowComic();
            accountFollowComic.setAccount(account);
            accountFollowComic.setComic(comic);
            accountFollowComicRepository.save(accountFollowComic);
            return comic;
        } throw new InvalidException(NumberError.FOLLOW_FAILED.getMessage(),  NumberError.FOLLOW_FAILED);
    }

    @Transactional
    public Comic handleUnfollowComic(String accountUuid, String comicUuid) {
        Account account =  accountRepository.findByUuid(accountUuid).orElseThrow(() -> new InvalidException(NumberError.ACCOUNT_NOT_FOUND.getMessage(), NumberError.ACCOUNT_NOT_FOUND));
        Comic comic = comicRepository.findComicByUuidComic(comicUuid).orElseThrow(() -> new InvalidException(NumberError.COMIC_NOT_FOUND.getMessage(), NumberError.COMIC_NOT_FOUND));
        if (accountFollowComicRepository.existsByAccountIdAndComicId(account.getId(), comic.getId())) {
            accountFollowComicRepository.deleteByAccountIdAndComicId(account.getId(), comic.getId());
            return comic;
        } throw new InvalidException(NumberError.UNFOLLOW_FAILED.getMessage(),  NumberError.UNFOLLOW_FAILED);
    }
}
