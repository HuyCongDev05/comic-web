package com.example.projectrestfulapi.dto.response.user;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
public class UserLoginResponseDTO {
    private String uuid;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private String avatar;
    private String status;
    private List<providers> providers;
    private String accessToken;

    @Getter
    @Setter
    public static class providers {
        private String provider;
        private Instant LinkedAt;
    }
}
