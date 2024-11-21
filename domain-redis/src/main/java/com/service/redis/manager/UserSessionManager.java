package com.service.redis.manager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.redis.cache.UserSessionCache;
import com.service.redis.exception.RedisErrorCode;
import com.service.redis.exception.RedisException;
import com.service.redis.repository.UserSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserSessionManager {
    private static final String SESSION_KEY_PREFIX = "session:";
    private static final long LOCK_WAIT_TIME = 500;
    private static final long LOCK_LEASE_TIME = 1000;

    private final UserSessionRepository sessionRepository;
    private final RedissonClient redissonClient;
    private final ObjectMapper objectMapper;

    /**
     * 세션 저장
     */
    public void saveSession(UserSessionCache session, Duration duration) {
        String lockKey = getLockKey(session.getSessionId());
        RLock lock = redissonClient.getLock(lockKey);

        try {
            if (tryAcquireLock(lock)) {
                try {
                    String key = getSessionKey(session.getSessionId());
                    String serialized = serializeSession(session);
                    sessionRepository.setValue(key, serialized, duration);
                    log.debug("Saved session: {}", session.getSessionId());
                } finally {
                    releaseLock(lock);
                }
            }
        } catch (Exception e) {
            log.error("Failed to save session: {}", session.getSessionId(), e);
            throw new RedisException(RedisErrorCode.SESSION_SAVE_FAILED);
        }
    }

    /**
     * 세션 ID로 조회
     */
    public Optional<UserSessionCache> findBySessionId(String sessionId) {
        try {
            String key = getSessionKey(sessionId);
            String value = sessionRepository.getValue(key);

            if (value == null) {
                return Optional.empty();
            }

            UserSessionCache session = deserializeSession(value);
            if (session != null) {
                session.updateLastAccessedAt();
                updateSession(session);
            }

            return Optional.ofNullable(session);
        } catch (Exception e) {
            log.error("Failed to find session: {}", sessionId, e);
            throw new RedisException(RedisErrorCode.SESSION_SAVE_FAILED);
        }
    }

    /**
     * 세션 업데이트
     */
    public void updateSession(UserSessionCache session) {
        String lockKey = getLockKey(session.getSessionId());
        RLock lock = redissonClient.getLock(lockKey);

        try {
            if (tryAcquireLock(lock)) {
                try {
                    String key = getSessionKey(session.getSessionId());
                    Duration remainingTtl = getSessionTtl(key);

                    if (remainingTtl != null) {
                        String serialized = serializeSession(session);
                        sessionRepository.setValue(key, serialized, remainingTtl);
                    }
                } finally {
                    releaseLock(lock);
                }
            }
        } catch (Exception e) {
            log.error("Failed to update session: {}", session.getSessionId(), e);
            throw new RedisException(RedisErrorCode.SESSION_SAVE_FAILED);
        }
    }

    /**
     * 세션 삭제
     */
    public void deleteSession(String sessionId) {
        try {
            String key = getSessionKey(sessionId);
            sessionRepository.deleteKey(key);
            log.debug("Deleted session: {}", sessionId);
        } catch (Exception e) {
            log.error("Failed to delete session: {}", sessionId, e);
            throw new RedisException(RedisErrorCode.SESSION_SAVE_FAILED);
        }
    }

    /**
     * 세션 존재 여부 확인
     */
    public boolean hasSession(String sessionId) {
        try {
            String key = getSessionKey(sessionId);
            return sessionRepository.hasKey(key);
        } catch (Exception e) {
            log.error("Failed to check session existence: {}", sessionId, e);
            throw new RedisException(RedisErrorCode.SESSION_SAVE_FAILED);
        }
    }

    /**
     * 세션 만료 시간 연장
     */
    public void extendSession(String sessionId, Duration duration) {
        String lockKey = getLockKey(sessionId);
        RLock lock = redissonClient.getLock(lockKey);

        try {
            if (tryAcquireLock(lock)) {
                try {
                    String key = getSessionKey(sessionId);
                    sessionRepository.expire(key, duration);
                    log.debug("Extended session: {}", sessionId);
                } finally {
                    releaseLock(lock);
                }
            }
        } catch (Exception e) {
            log.error("Failed to extend session: {}", sessionId, e);
            throw new RedisException(RedisErrorCode.SESSION_SAVE_FAILED);
        }
    }

    private String serializeSession(UserSessionCache session) {
        try {
            return objectMapper.writeValueAsString(session);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize session: {}", session.getSessionId(), e);
            throw new RedisException(RedisErrorCode.SESSION_SAVE_FAILED);
        }
    }

    private UserSessionCache deserializeSession(String value) {
        try {
            return objectMapper.readValue(value, UserSessionCache.class);
        } catch (JsonProcessingException e) {
            log.error("Failed to deserialize session value: {}", value, e);
            throw new RedisException(RedisErrorCode.SESSION_SAVE_FAILED);
        }
    }

    private String getSessionKey(String sessionId) {
        return SESSION_KEY_PREFIX + sessionId;
    }

    private String getLockKey(String sessionId) {
        return "lock:session:" + sessionId;
    }

    private Duration getSessionTtl(String key) {
        Long ttl = sessionRepository.getExpire(key);
        return ttl != null && ttl > 0 ? Duration.ofSeconds(ttl) : null;
    }

    private boolean tryAcquireLock(RLock lock) {
        try {
            return lock.tryLock(LOCK_WAIT_TIME, LOCK_LEASE_TIME, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Lock acquisition was interrupted", e);
            return false;
        }
    }

    private void releaseLock(RLock lock) {
        if (lock.isHeldByCurrentThread()) {
            lock.unlock();
        }
    }
}
