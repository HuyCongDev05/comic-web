package com.example.projectrestfulapi.controller;

import com.example.projectrestfulapi.exception.InvalidException;
import com.example.projectrestfulapi.exception.NumberError;
import com.example.projectrestfulapi.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @DeleteMapping("/accounts")
    public ResponseEntity<Void> deleteUser(@RequestParam(name = "account_uuid") String accountUuid) {
        if (!accountService.handleDeleteAccount(accountUuid))
            throw new InvalidException(NumberError.USER_NOT_FOUND.getMessage(), NumberError.USER_NOT_FOUND);
        return ResponseEntity.ok().build();
    }
}
