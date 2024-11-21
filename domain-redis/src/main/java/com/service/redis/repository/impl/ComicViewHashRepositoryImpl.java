package com.service.redis.repository.impl;

import com.service.redis.repository.ComicViewHashRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ComicViewHashRepositoryImpl implements ComicViewHashRepository {

    private final RedisTemplate<String, String> redisTemplate;
    @Value("${cache.comic.hash.view.key}")
    private String viewHashKey;

    @Override
    public boolean saveViewHashIfNotExists(String hash, LocalDate date) {
        return Boolean.TRUE.equals(redisTemplate.opsForSet().add(viewHashKey, hash));
    }

    @Override
    public boolean existsViewHash(String hash, LocalDate date) {
        return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(viewHashKey, hash));
    }

    @Override
    public void deleteViewHash(String hash) {
        redisTemplate.delete(viewHashKey);
    }
}
