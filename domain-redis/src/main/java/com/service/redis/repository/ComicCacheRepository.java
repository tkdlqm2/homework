package com.service.redis.repository;

import com.service.redis.cache.ComicCache;

import java.util.List;
import java.util.Set;

public interface ComicCacheRepository {
    void addToSortedSet(String key, String value, double score);
    Set<String> getTopN(String key, int n);
    void incrementScore(String key, String value, double increment);
}
