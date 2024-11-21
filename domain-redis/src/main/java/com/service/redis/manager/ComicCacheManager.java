package com.service.redis.manager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.core.domain.comic.ComicVo;
import com.service.redis.cache.ComicCache;
import com.service.redis.exception.RedisErrorCode;
import com.service.redis.exception.RedisException;
import com.service.redis.repository.ComicCacheRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class ComicCacheManager {

    private static final String SORTED_SET_KEY = "comic:view:count";
    private final ComicCacheRepository cacheRepository;
    private final RedissonClient redissonClient;
    private final ObjectMapper objectMapper;

    /**
     * 조회수 기준 상위 N개 작품 조회
     */
    public List<ComicCache> getTopViewedComics(int limit) {
        try {
            Set<String> rawComics = cacheRepository.getTopN(SORTED_SET_KEY, limit);
            return rawComics.stream()
                    .map(this::deserializeComic)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Failed to get top viewed comics", e);
            return Collections.emptyList();
        }
    }

    /**
     * 작품 조회수 증가
     */
    public void incrementViewCount(ComicVo comicVo) {
        try {
            String comicJson = serializeComic(convertToCache(comicVo));
            cacheRepository.incrementScore(SORTED_SET_KEY, comicJson, 1.0);
        } catch (Exception e) {
            log.error("Failed to increment comic view count: {}", comicVo.getId(), e);
        }
    }

    /**
     * DB 데이터로 SortedSet 갱신
     */
    public List<ComicCache> refreshTopComics(List<ComicVo> comics) {
        try {
            return comics.stream()
                    .map(comic -> {
                        ComicCache cache = convertToCache(comic);
                        String comicJson = serializeComic(cache);
                        cacheRepository.addToSortedSet(SORTED_SET_KEY, comicJson, comic.getViewCount());
                        return cache;
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Failed to refresh comic cache", e);
            return Collections.emptyList();
        }
    }

    private ComicCache convertToCache(ComicVo comic) {
        return ComicCache.builder()
                .comicId(comic.getId())
                .title(comic.getTitle())
                .authorName(comic.getAuthorName())
                .genres(comic.getGenres())
                .ageRating(comic.getAgeRating())
                .price(comic.getPrice())
                .event(comic.isEvent())
                .build();
    }

    private String serializeComic(ComicCache comic) {
        try {
            return objectMapper.writeValueAsString(comic);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize comic: {}", comic.getComicId(), e);
            throw new RedisException(RedisErrorCode.SERIALIZATION_FAILED);
        }
    }

    private Optional<ComicCache> deserializeComic(String json) {
        try {
            return Optional.of(objectMapper.readValue(json, ComicCache.class));
        } catch (JsonProcessingException e) {
            log.error("Failed to deserialize comic: {}", json, e);
            return Optional.empty();
        }
    }
}