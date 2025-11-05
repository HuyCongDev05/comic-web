package com.example.projectrestfulapi.service;

import com.example.projectrestfulapi.domain.SQL.Member;
import com.example.projectrestfulapi.repository.SQL.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }
}
