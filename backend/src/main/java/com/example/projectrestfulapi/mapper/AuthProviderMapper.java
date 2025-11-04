package com.example.projectrestfulapi.mapper;

import com.example.projectrestfulapi.domain.SQL.AuthProvider;
import com.example.projectrestfulapi.dto.response.user.UserLoginResponseDTO;

import java.util.List;

public class AuthProviderMapper {
    public static List<UserLoginResponseDTO.providers> providersMapper(List<AuthProvider> authProviders) {
        return authProviders.stream()
                .map(p -> {
                    UserLoginResponseDTO.providers dto = new UserLoginResponseDTO.providers();
                    dto.setProvider(p.getProvider());
                    dto.setLinkedAt(p.getLinkedAt());
                    return dto;
                })
                .toList();
    }
}
