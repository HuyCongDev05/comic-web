package com.example.projectrestfulapi.controller;

import com.example.projectrestfulapi.domain.SQL.User;
import com.example.projectrestfulapi.dto.request.User.UserUpdateRequestDTO;
import com.example.projectrestfulapi.exception.InvalidException;
import com.example.projectrestfulapi.exception.NumberError;
import com.example.projectrestfulapi.service.AccountService;
import com.example.projectrestfulapi.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    private final AccountService accountService;
    private final UserService userService;

    public UserController(AccountService accountService, UserService userService) {
        this.accountService = accountService;
        this.userService = userService;
    }

    @GetMapping("/list/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok().body(userService.handleFindAllUsers());
    }

    @PutMapping("/update/users")
    public ResponseEntity<User> updateUser(@Valid @RequestBody UserUpdateRequestDTO updateUserRequestDTO) {
        return ResponseEntity.ok().body(userService.handleUpdateUser(updateUserRequestDTO));
    }

    @DeleteMapping("/delete/accounts/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") @Min(1) Long id) {
        if (!accountService.handleDeleteAccount(id))
            throw new InvalidException(NumberError.USER_NOT_FOUND.getMessage(), NumberError.USER_NOT_FOUND);
        return ResponseEntity.ok().build();
    }
}
