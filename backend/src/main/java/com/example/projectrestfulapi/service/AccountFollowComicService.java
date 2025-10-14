package com.example.projectrestfulapi.service;

import com.example.projectrestfulapi.domain.SQL.AccountFollowComic;
import com.example.projectrestfulapi.domain.SQL.Comic;
import com.example.projectrestfulapi.repository.SQL.AccountFollowComicRepository;
import com.example.projectrestfulapi.repository.SQL.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountFollowComicService {
    private final AccountFollowComicRepository accountFollowComicRepository;
    private final AccountRepository accountRepository;

    public AccountFollowComicService(AccountFollowComicRepository accountFollowComicRepository, AccountRepository accountRepository) {
        this.accountFollowComicRepository = accountFollowComicRepository;
        this.accountRepository = accountRepository;
    }

    public List<Comic> handleGetListAccountFollowComicByAccountId(String uuid) {
        Long accountId = accountRepository.findAccountIdByUuid(uuid);
        List<AccountFollowComic> accountFollowComics = accountFollowComicRepository.findByAccountId(accountId);
        return accountFollowComics.stream().map(AccountFollowComic::getComic).collect(Collectors.toList());
    }
}
