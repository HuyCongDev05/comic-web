package com.example.projectrestfulapi.controller;

import com.example.projectrestfulapi.domain.SQL.Member;
import com.example.projectrestfulapi.dto.response.member.MemberResponseDTO;
import com.example.projectrestfulapi.mapper.MemberMapper;
import com.example.projectrestfulapi.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/members")
    public ResponseEntity<List<MemberResponseDTO>> findAll() {
        List<Member> members = memberService.findAll();

        List<MemberResponseDTO> responseList = new ArrayList<>();
        for (Member m : members) {
            responseList.add(MemberMapper.memberToMemberResponseDTO(m));
        }
        return ResponseEntity.ok().body(responseList);
    }
}
