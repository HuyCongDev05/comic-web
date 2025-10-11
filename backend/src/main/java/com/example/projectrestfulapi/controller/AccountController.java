package com.example.projectrestfulapi.controller;

import com.example.projectrestfulapi.exception.InvalidException;
import com.example.projectrestfulapi.exception.NumberError;
import com.example.projectrestfulapi.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api/v1")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @DeleteMapping("/accounts-delete")
    public ResponseEntity<Void> deleteUser(@RequestParam(name = "uuid") String uuid) {
        if (!accountService.handleDeleteAccount(uuid))
            throw new InvalidException(NumberError.USER_NOT_FOUND.getMessage(), NumberError.USER_NOT_FOUND);
        return ResponseEntity.ok().build();
    }
}
