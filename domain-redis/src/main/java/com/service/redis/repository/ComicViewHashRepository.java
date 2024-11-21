package com.service.redis.repository;

import java.time.LocalDate;

public interface ComicViewHashRepository {
    /**
     * 해시값 존재 여부 확인 및 저장
     */
    boolean saveViewHashIfNotExists(String hash, LocalDate date);

    /**
     * 해시값 존재 여부 확인
     */
    boolean existsViewHash(String hash, LocalDate date);

    /**
     * 특정 날짜의 모든 해시값 삭제
     */
    void deleteViewHash(String hash);
}
