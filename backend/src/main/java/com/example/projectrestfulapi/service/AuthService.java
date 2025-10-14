package com.example.projectrestfulapi.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class AuthService {
    private final StringRedisTemplate stringRedisTemplate;

    public AuthService(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public void handleSave(String username, String refreshToken) {
        stringRedisTemplate.opsForValue().set("refreshToken:" + username, refreshToken, 7, TimeUnit.DAYS);
    }

    public void handleRefreshSave(String username, String refreshToken) {
        String key = "refreshToken:" + username;
        Long expired = stringRedisTemplate.getExpire(key, TimeUnit.SECONDS);
        stringRedisTemplate.opsForValue().set(key, refreshToken, expired, TimeUnit.SECONDS);
    }

    public boolean handleExistsByUsername(String username) {
        String key = "refreshToken:" + username;
        return stringRedisTemplate.hasKey(key);
    }

    public void handleDelete(String username) {
        String key = "refreshToken:" + username;
        stringRedisTemplate.delete(key);
    }
}
