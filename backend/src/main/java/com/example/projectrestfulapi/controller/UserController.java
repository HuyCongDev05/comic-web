package com.example.projectrestfulapi.controller;

import com.example.projectrestfulapi.domain.SQL.Member;
import com.example.projectrestfulapi.domain.SQL.User;
import com.example.projectrestfulapi.dto.request.User.UserUpdateRequestDTO;
import com.example.projectrestfulapi.dto.response.member.MemberResponseDTO;
import com.example.projectrestfulapi.dto.response.user.UserUpdateResponseDTO;
import com.example.projectrestfulapi.mapper.MemberMapper;
import com.example.projectrestfulapi.mapper.UserMapper;
import com.example.projectrestfulapi.service.AccountService;
import com.example.projectrestfulapi.service.MemberService;
import com.example.projectrestfulapi.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;
    private final AccountService accountService;
    private final MemberService memberService;

    public UserController(UserService userService, AccountService accountService, MemberService memberService) {
        this.userService = userService;
        this.accountService = accountService;
        this.memberService = memberService;
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

    @GetMapping("/members")
    public ResponseEntity<List<MemberResponseDTO>> getAllMembers() {
        List<Member> memberList = memberService.findAll();
        List<MemberResponseDTO> memberResponseDTOList = new ArrayList<>();
        for (Member member : memberList) {
            memberResponseDTOList.add(MemberMapper.memberToMemberResponseDTO(member));
        }

        return ResponseEntity.ok().body(memberResponseDTOList);
    }
}
