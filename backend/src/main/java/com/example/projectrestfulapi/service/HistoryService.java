package com.example.projectrestfulapi.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class HistoryService {
    private final StringRedisTemplate stringRedisTemplate;

    public HistoryService(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public void handleSave(String accountUuid, String comicUuid) {
        String key = "history:" + accountUuid;

        stringRedisTemplate.opsForList().remove(key, 0, comicUuid);

        stringRedisTemplate.opsForList().leftPush(key, comicUuid);
        stringRedisTemplate.opsForList().trim(key, 0, 71);

        stringRedisTemplate.expire(key, 7, TimeUnit.DAYS);
    }

    public List<String> handleFindByPage(String accountUuid, int page) {
        String key = "history:" + accountUuid;

        int size = 24;
        int start = page * size;
        int end = start + size - 1;

        return stringRedisTemplate.opsForList().range(key, start, end);
    }
}
