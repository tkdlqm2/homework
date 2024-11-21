package com.service.redis.repository.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.redis.cache.UserSessionCache;
import com.service.redis.exception.RedisErrorCode;
import com.service.redis.exception.RedisException;
import com.service.redis.repository.UserSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UserSessionRepositoryImpl implements UserSessionRepository {
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void setValue(String key, String value, Duration duration) {
        redisTemplate.opsForValue().set(key, value, duration);
    }

    @Override
    public String getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void deleteKey(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    @Override
    public Boolean expire(String key, Duration duration) {
        return redisTemplate.expire(key, duration);
    }

    @Override
    public Long getExpire(String key) {
        return redisTemplate.getExpire(key);
    }
}
