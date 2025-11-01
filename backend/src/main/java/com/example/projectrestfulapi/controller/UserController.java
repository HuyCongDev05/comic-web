package com.example.projectrestfulapi.controller;

import com.example.projectrestfulapi.domain.SQL.User;
import com.example.projectrestfulapi.dto.request.User.UserUpdateRequestDTO;
import com.example.projectrestfulapi.dto.response.user.UserUpdateResponseDTO;
import com.example.projectrestfulapi.mapper.UserMapper;
import com.example.projectrestfulapi.service.AccountService;
import com.example.projectrestfulapi.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;
    private final AccountService accountService;

    public UserController(UserService userService, AccountService accountService) {
        this.userService = userService;
        this.accountService = accountService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok().body(userService.handleGetAllUsers());
    }

    @PutMapping("users")
    public ResponseEntity<UserUpdateResponseDTO> updateUser(@RequestParam("uuid") String uuid, @Valid @RequestBody UserUpdateRequestDTO updateUserRequestDTO) {
        User user = userService.handleUpdateUser(uuid, updateUserRequestDTO);
        String avatar = accountService.handleGetAvatarByUuid(uuid);
        UserUpdateResponseDTO userUpdateResponseDTO = UserMapper.mapUserUpdateResponseDTO(user, avatar);
        return ResponseEntity.ok().body(userUpdateResponseDTO);
    }
}
