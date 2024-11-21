package com.service.redis.manager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.redis.cache.ComicViewCache;
import com.service.redis.exception.RedisErrorCode;
import com.service.redis.exception.RedisException;
import com.service.redis.repository.ComicViewQueueRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class ComicViewQueueManager {
    private static final String QUEUE_KEY_PREFIX = "comic:view:queue:";
    private static final String ACTIVE_QUEUE_KEY = "comic:view:active-queues";

    private final ComicViewQueueRepository queueRepository;
    private final ObjectMapper objectMapper;

    public void pushToQueue(ComicViewCache viewCache) {
        try {
            String queueKey = generateKey(viewCache.getComicId());
            String cacheValue = serializeCache(viewCache);
            String activeValue = String.valueOf(viewCache.getComicId());

            queueRepository.pushToQueue(queueKey, cacheValue, ACTIVE_QUEUE_KEY, activeValue);
            log.debug("Successfully pushed to queue - comicId: {}", viewCache.getComicId());
        } catch (Exception e) {
            log.error("Failed to push to queue - comicId: {}", viewCache.getComicId(), e);
            throw new RedisException(RedisErrorCode.QUEUE_PUSH_FAILED);
        }
    }

    public Set<Long> getActiveQueueComicIds() {
        try {
            Set<String> comicIds = queueRepository.getSetMembers(ACTIVE_QUEUE_KEY);
            return comicIds.stream()
                    .map(Long::parseLong)
                    .collect(Collectors.toSet());
        } catch (Exception e) {
            log.error("Failed to get active queue comic ids", e);
            return Collections.emptySet();
        }
    }

    public List<ComicViewCache> popBulk(Long comicId, int count) {
        try {
            String queueKey = generateKey(comicId);
            return queueRepository.bulkLeftPop(queueKey, count).stream()
                    .map(this::deserializeCache)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Failed to bulk pop from queue - comicId: {}", comicId, e);
            return Collections.emptyList();
        }
    }

    public void cleanupEmptyQueues() {
        try {
            Set<Long> activeComicIds = getActiveQueueComicIds();
            for (Long comicId : activeComicIds) {
                String queueKey = generateKey(comicId);
                Long size = queueRepository.getListSize(queueKey);

                if (size != null && size == 0) {
                    queueRepository.removeFromSet(ACTIVE_QUEUE_KEY, String.valueOf(comicId));
                    queueRepository.delete(queueKey);
                    log.debug("Cleaned up empty queue - comicId: {}", comicId);
                }
            }
        } catch (Exception e) {
            log.error("Failed to cleanup empty queues", e);
        }
    }

    private String generateKey(Long comicId) {
        return QUEUE_KEY_PREFIX + comicId;
    }

    private String serializeCache(ComicViewCache cache) {
        try {
            return objectMapper.writeValueAsString(cache);
        } catch (JsonProcessingException e) {
            throw new RedisException(RedisErrorCode.SERIALIZATION_FAILED);
        }
    }

    private Optional<ComicViewCache> deserializeCache(String json) {
        try {
            return Optional.of(objectMapper.readValue(json, ComicViewCache.class));
        } catch (JsonProcessingException e) {
            log.error("Failed to deserialize cache: {}", json, e);
            return Optional.empty();
        }
    }
}