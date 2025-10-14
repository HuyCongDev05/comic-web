package com.example.projectrestfulapi.mapper;

import com.example.projectrestfulapi.dto.response.token.RefreshToken;

public class RefreshTokenMapper {
    public static RefreshToken mapRefreshToken(String accessToken, String refreshToken) {
        if (refreshToken == null) return null;
        RefreshToken token = new RefreshToken();
        token.setAccessToken(accessToken);
        token.setRefreshToken(refreshToken);
        return token;
    }
}
