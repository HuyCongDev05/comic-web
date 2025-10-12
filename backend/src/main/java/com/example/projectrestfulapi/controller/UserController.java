package com.example.projectrestfulapi.controller;

import com.example.projectrestfulapi.domain.SQL.User;
import com.example.projectrestfulapi.dto.request.User.UserUpdateRequestDTO;
import com.example.projectrestfulapi.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok().body(userService.handleFindAllUsers());
    }

    @PutMapping("users/{uuid}")
    public ResponseEntity<User> updateUser( @PathVariable("uuid") String uuid, @Valid @RequestBody UserUpdateRequestDTO updateUserRequestDTO) {
        return ResponseEntity.ok().body(userService.handleUpdateUser(uuid, updateUserRequestDTO));
    }
}
