package com.service.redis.cache;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
/**
 * 1. 세션 키 구조
 */
@Getter
@Builder
@RedisHash("user_session_cache")
@NoArgsConstructor
@AllArgsConstructor
public class UserSessionCache implements Serializable {
    private String sessionId;    // Redis Key로 사용될 세션 ID
    private Long userId;         // 사용자 ID
    private String email;        // 사용자 이메일
    private LocalDateTime lastAccessedAt;  // 마지막 접근 시간
    private Map<String, String> attributes;  // 추가 세션 데이터

    public void updateLastAccessedAt() {
        this.lastAccessedAt = LocalDateTime.now();
    }
}
