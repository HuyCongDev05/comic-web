package com.example.projectrestfulapi.dto.response.member;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberResponseDTO {
    private Long id;
    private String name;
    private String avatar;
    private String facebook;
    private String instagram;
    private String x;
    private String role;
}
