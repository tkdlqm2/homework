package com.service.redis.repository.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.redis.cache.ComicCache;
import com.service.redis.exception.RedisErrorCode;
import com.service.redis.exception.RedisException;
import com.service.redis.repository.ComicPurchaseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ComicPurchaseRepositoryImpl implements ComicPurchaseRepository {
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void addToSortedSet(String key, String value, double score) {
        redisTemplate.opsForZSet().add(key, value, score);
    }

    @Override
    public Set<ZSetOperations.TypedTuple<String>> getTopNWithScores(String key, int n) {
        return redisTemplate.opsForZSet().reverseRangeWithScores(key, 0, n - 1);
    }

    @Override
    public Boolean exists(String key, String value) {
        return redisTemplate.opsForZSet().score(key, value) != null;
    }

    @Override
    public Double incrementScore(String key, String value, double increment) {
        return redisTemplate.opsForZSet().incrementScore(key, value, increment);
    }

    @Override
    public Long size(String key) {
        return redisTemplate.opsForZSet().size(key);
    }
}

