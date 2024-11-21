package com.service.redis.repository;

import com.service.redis.cache.ComicCache;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.List;
import java.util.Set;

public interface ComicPurchaseRepository {
    void addToSortedSet(String key, String value, double score);
    Set<ZSetOperations.TypedTuple<String>> getTopNWithScores(String key, int n);
    Boolean exists(String key, String value);
    Double incrementScore(String key, String value, double increment);
    Long size(String key);
}
