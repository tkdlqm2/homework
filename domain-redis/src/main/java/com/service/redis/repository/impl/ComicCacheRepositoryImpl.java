package com.service.redis.repository.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.redis.cache.ComicCache;
import com.service.redis.exception.RedisErrorCode;
import com.service.redis.exception.RedisException;
import com.service.redis.repository.ComicCacheRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ComicCacheRepositoryImpl implements ComicCacheRepository {
    private final RedisTemplate<String, String> redisTemplate;

    // 단순 Redis 작업만 수행
    @Override
    public void addToSortedSet(String key, String value, double score) {
        redisTemplate.opsForZSet().add(key, value, score);
    }

    @Override
    public Set<String> getTopN(String key, int n) {
        return redisTemplate.opsForZSet().reverseRange(key, 0, n - 1);
    }

    @Override
    public void incrementScore(String key, String value, double increment) {
        redisTemplate.opsForZSet().incrementScore(key, value, increment);
    }
}