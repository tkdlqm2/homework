package com.service.redis.repository;

import com.service.redis.cache.UserSessionCache;

import java.time.Duration;
import java.util.Optional;

public interface UserSessionRepository {
    void setValue(String key, String value, Duration duration);
    String getValue(String key);
    void deleteKey(String key);
    Boolean hasKey(String key);
    Boolean expire(String key, Duration duration);
    Long getExpire(String key);
}