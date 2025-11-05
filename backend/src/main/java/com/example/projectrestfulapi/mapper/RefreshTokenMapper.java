package com.example.projectrestfulapi.mapper;

import com.example.projectrestfulapi.dto.response.token.RefreshToken;

public class RefreshTokenMapper {
    public static RefreshToken mapRefreshToken(String accessToken) {
        if (accessToken == null) return null;
        RefreshToken token = new RefreshToken();
        token.setAccessToken(accessToken);
        return token;
    }
}
