package com.service.redis.manager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.core.domain.comic.ComicVo;
import com.service.redis.cache.ComicCache;

import com.service.redis.exception.RedisErrorCode;
import com.service.redis.exception.RedisException;
import com.service.redis.repository.ComicPurchaseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.ZSetOperations;
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
public class ComicPurchaseManager {
    private static final String PURCHASE_COUNT_KEY = "comic:purchase:count";

    private final ComicPurchaseRepository purchaseRepository;
    private final ObjectMapper objectMapper;
    private final RedissonClient redissonClient;

    /**
     * 구매수 기준 상위 N개 작품 조회
     */
    public List<ComicCache> getTopPurchasedComics(int limit) {
        try {
            Set<ZSetOperations.TypedTuple<String>> tuples =
                    purchaseRepository.getTopNWithScores(PURCHASE_COUNT_KEY, limit);

            if (tuples == null || tuples.isEmpty()) {
                return Collections.emptyList();
            }

            return tuples.stream()
                    .map(tuple -> deserializeComic(tuple.getValue()))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Failed to get top purchased comics", e);
            return Collections.emptyList();
        }
    }

    /**
     * 작품 구매수 증가
     */
    public void incrementPurchaseCount(ComicVo comicVo) {

        try {
            ComicCache comicCache = convertToCache(comicVo);
            String comicJson = serializeComic(comicCache);

            // 존재하지 않으면 초기값으로 추가
            if (!purchaseRepository.exists(PURCHASE_COUNT_KEY, comicJson)) {
                purchaseRepository.addToSortedSet(PURCHASE_COUNT_KEY, comicJson, 0);
                log.info("Added new comic to purchase ranking: {}", comicVo.getId());
            }

            // 구매수 증가
            Double newScore = purchaseRepository.incrementScore(PURCHASE_COUNT_KEY, comicJson, 1);
            log.debug("Incremented purchase count for comic {}: new score {}",
                    comicVo.getId(), newScore);
        } catch (Exception e) {
            log.error("Failed to increment purchase count for comic: {}", comicVo.getId(), e);
        }
    }

    /**
     * DB 데이터로 SortedSet 갱신
     */
    public void refreshPurchaseRanking(List<ComicVo> comics) {
        try {
            comics.forEach(comic -> {
                ComicCache cache = convertToCache(comic);
                String comicJson = serializeComic(cache);
                purchaseRepository.addToSortedSet(PURCHASE_COUNT_KEY, comicJson, comic.getPurchaseCount());
            });
        } catch (Exception e) {
            log.error("Failed to refresh purchase ranking", e);
        }
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
}

