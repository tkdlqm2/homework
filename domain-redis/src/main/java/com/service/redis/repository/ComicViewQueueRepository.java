package com.service.redis.repository;

import com.service.redis.cache.ComicViewCache;

import java.util.List;
import java.util.Set;

public interface ComicViewQueueRepository {
    void pushToQueue(String queueKey, String value, String activeQueueKey, String activeValue);
    Set<String> getSetMembers(String key);
    String leftPop(String key);
    Long getListSize(String key);
    void removeFromSet(String key, String value);
    Boolean isMember(String key, String value);
    void delete(String key);
    List<String> bulkLeftPop(String key, int count);
    Boolean hasKey(String key);

}
