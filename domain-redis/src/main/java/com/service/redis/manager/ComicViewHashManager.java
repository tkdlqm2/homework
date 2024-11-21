package com.service.redis.manager;

import com.service.core.domain.comic.ComicViewRequest;
import com.service.redis.repository.ComicViewHashRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
@Slf4j
public class ComicViewHashManager {

    private final ComicViewHashRepository viewHashRepository;
    private static final Duration RETENTION_PERIOD = Duration.ofDays(7); // 보관 기간

    /**
     * 조회 해시 처리
     */
    public boolean processViewHash(String viewHash) {
        LocalDate today = LocalDate.now();

        try {
            // 해시값이 없을 경우에만 true 반환
            return viewHashRepository.saveViewHashIfNotExists(viewHash, today);
        } catch (Exception e) {
            log.error("Failed to process view hash: {}", viewHash, e);
            // 에러 발생 시 조회 허용 (fail-open 정책)
            return true;
        }
    }

    /**
     * 조회 해시 검증
     */
    public boolean validateView(String viewHash) {
        LocalDate today = LocalDate.now();

        try {
            // 이미 존재하면 false (중복 조회)
            return !viewHashRepository.existsViewHash(viewHash, today);
        } catch (Exception e) {
            log.error("Failed to validate view: {}", viewHash, e);
            // 에러 발생 시 조회 허용
            return true;
        }
    }

//    /**
//     * 오래된 해시 데이터 정리
//     */
//    @Scheduled(cron = "0 0 0 * * *") // 매일 자정
//    public void cleanupOldHashes() {
//        LocalDate deletionDate = LocalDate.now().minus(RETENTION_PERIOD);
//        try {
//            viewHashRepository.deleteViewHash(deletionDate);
//            log.info("Cleaned up view hashes for date: {}", deletionDate);
//        } catch (Exception e) {
//            log.error("Failed to cleanup old hashes for date: {}", deletionDate, e);
//        }
//    }

    /**
     * 해시 생성
     */
    public String generateViewHash(ComicViewRequest request) {
        String combined = String.format("%d:%d:%s:%s:%s",
                request.getComicId(),
                request.getUserId(),
                request.getDeviceInfo(),
                request.getIpAddress(),
                LocalDate.now().format(DateTimeFormatter.ISO_DATE)
        );
        return DigestUtils.sha256Hex(combined);
    }
}

