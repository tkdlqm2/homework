package com.service.redis.repository.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.redis.cache.ComicViewCache;
import com.service.redis.exception.RedisErrorCode;
import com.service.redis.exception.RedisException;
import com.service.redis.repository.ComicViewQueueRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ComicViewQueueRepositoryImpl implements ComicViewQueueRepository {
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public Set<String> getSetMembers(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    @Override
    public String leftPop(String key) {
        return redisTemplate.opsForList().leftPop(key);
    }

    @Override
    public Long getListSize(String key) {
        return redisTemplate.opsForList().size(key);
    }

    @Override
    public void removeFromSet(String key, String value) {
        redisTemplate.opsForSet().remove(key, value);
    }

    @Override
    public Boolean isMember(String key, String value) {
        return redisTemplate.opsForSet().isMember(key, value);
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    @Override
    public List<String> bulkLeftPop(String key, int count) {
        List<String> result = new ArrayList<>();
        redisTemplate.execute((RedisCallback<Void>) connection -> {
            connection.openPipeline();
            for (int i = 0; i < count; i++) {
                connection.lPop(key.getBytes());
            }
            List<Object> pipelineResult = connection.closePipeline();
            pipelineResult.stream()
                    .filter(Objects::nonNull)
                    .map(data -> new String((byte[]) data))
                    .forEach(result::add);
            return null;
        });
        return result;
    }


    @Override
    public void pushToQueue(String queueKey, String value, String activeQueueKey, String activeValue) {
        redisTemplate.execute(new SessionCallback<>() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                operations.multi();
                operations.opsForList().rightPush(queueKey, value);
                operations.opsForSet().add(activeQueueKey, activeValue);
                return operations.exec();
            }
        });
    }

}