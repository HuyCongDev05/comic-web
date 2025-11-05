package com.example.projectrestfulapi.mapper;

import com.example.projectrestfulapi.domain.SQL.Member;
import com.example.projectrestfulapi.domain.SQL.Role;
import com.example.projectrestfulapi.dto.response.member.MemberResponseDTO;

import java.util.stream.Collectors;

public class MemberMapper {
    public static MemberResponseDTO memberToMemberResponseDTO(Member member) {
        if (member == null) return null;
        String role = member.getRoles().stream()
                .map(Role::getRoleName)
                .collect(Collectors.joining(" & "));
        MemberResponseDTO memberResponseDTO = new MemberResponseDTO();
        memberResponseDTO.setId(member.getId());
        memberResponseDTO.setName(member.getName());
        memberResponseDTO.setAvatar(member.getAvatar());
        memberResponseDTO.setFacebook(member.getFacebook());
        memberResponseDTO.setInstagram(member.getInstagram());
        memberResponseDTO.setX(member.getX());
        memberResponseDTO.setRole(role);
        return memberResponseDTO;
    }
}
