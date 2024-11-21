package com.service.service.comic.impl;

import com.service.core.domain.comic.ComicViewRequest;
import com.service.core.domain.comic.ComicVo;
import com.service.database.entity.comic.Comic;
import com.service.database.entity.comic.ComicViewLog;
import com.service.database.entity.comic.ComicViewStats;
import com.service.database.entity.user.User;
import com.service.database.repository.comic.ComicRepository;
import com.service.database.repository.comic.ComicViewLogRepository;
import com.service.database.repository.comic.ComicViewStatsRepository;
import com.service.database.repository.comic.impl.ComicViewLogRepositoryCustomImpl;
import com.service.database.repository.user.UserRepository;
import com.service.exception.comicViewLog.ComicViewLogErrorCode;
import com.service.exception.comicViewStats.ComicViewStatsException;
import com.service.redis.cache.ComicCache;
import com.service.redis.cache.ComicViewCache;
import com.service.redis.manager.ComicCacheManager;
import com.service.redis.manager.ComicViewHashManager;
import com.service.redis.manager.ComicViewQueueManager;
import com.service.service.comic.ComicViewCountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ComicViewCountServiceImpl implements ComicViewCountService {
    private final UserRepository userRepository;
    private final ComicRepository comicRepository;
    private final ComicViewHashManager comicViewHashManager;
    private final ComicViewLogRepository comicViewLogRepository;
    private final ComicViewLogRepositoryCustomImpl comicViewLogRepositoryCustom;
    private final ComicViewQueueManager comicViewQueueManager;
    private final ComicCacheManager comicCacheManager;
    private final ComicViewStatsRepository comicViewStatsRepository;
    private static final int requestSize = 10;

    @Override
    public List<ComicVo> getViewStatistics() {
        // 캐싱 조회
        List<ComicCache> comicCaches = comicCacheManager.getTopViewedComics(requestSize);
        List<ComicViewStats> comicViewStats = new ArrayList<>();
        // 조회 상위 10위권 DB로 부터 가져오기
        if(comicCaches.size() < requestSize) {
            comicViewStats = comicViewStatsRepository.findTopNByStatsDateOrderByDailyViewCountDesc(LocalDate.now().minusDays(1), requestSize);
            if (comicViewStats.isEmpty()) {
                log.error("No comicView stats found for date: {}", LocalDate.now());
                throw new ComicViewStatsException(ComicViewLogErrorCode.NOT_FOUND_VIEW_LOG);
            }
            List<Long> comicIds = comicViewStats.stream()
                    .map(stats -> stats.getComic().getId())
                    .collect(Collectors.toList());
            List<Comic> comics = comicRepository.findByIdIn(comicIds);
            Map<Long, Comic> comicMap = comics.stream()
                    .collect(Collectors.toMap(Comic::getId, comic -> comic));

            List<Comic> orderedComics = comicIds.stream()
                    .map(comicMap::get)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            int targetSize = orderedComics.size() >= requestSize ? requestSize : orderedComics.size();

            List<ComicVo> comicVos = new ArrayList<>();
            for(int i = 0; i < targetSize; i++) {
                Comic target_c = comics.get(i);
                ComicViewStats target_p = comicViewStats.get(i);
                comicVos.add(Comic.from(target_c, target_p));
            }

            // 조회 상위권 10개 캐싱
            comicCaches = comicCacheManager.refreshTopComics(comicVos);
        }
        return comicCaches.stream().map(ComicCache::toVo).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public boolean aggregateComicViewCount() {
        // 1. 활성화된 큐 ID들 조회
        Set<Long> comicIds = comicViewQueueManager.getActiveQueueComicIds();
        if (comicIds.isEmpty()) return true;

        List<ComicViewLog> allLogs = new ArrayList<>();

        // 2. 코믹별로 처리
        for (Long comicId : comicIds) {
            // 3. 배치 단위로 뷰 로그 처리
            List<ComicViewCache> viewCaches = comicViewQueueManager.popBulk(comicId, 1000);
            if (viewCaches.isEmpty()) continue;

            // 4. 필요한 Comic, User 정보를 한 번에 조회
            Set<Long> userIds = viewCaches.stream()
                    .map(ComicViewCache::getUserId)
                    .collect(Collectors.toSet());

            Map<Long, Comic> comicMap = comicRepository.findAllById(List.of(comicId))
                    .stream()
                    .collect(Collectors.toMap(Comic::getId, comic -> comic));

            Map<Long, User> userMap = userRepository.findAllById(userIds)
                    .stream()
                    .collect(Collectors.toMap(User::getId, user -> user));

            // Cache -> common Vo
            List<ComicViewRequest> comicViewRequests = new ArrayList<>();
            for(ComicViewCache comicViewCache: viewCaches) {
                comicViewCache.setIpAddress("test");
                comicViewCache.setDeviceInfo("test");
                comicViewRequests.add(ComicViewCache.from(comicViewCache));
            }


            // 5. 캐시 데이터를 로그로 변환
            // commonVo -> entity
            List<ComicViewLog> logs = comicViewRequests.stream()
                    .map(cache -> ComicViewLog.from(cache,
                            comicMap.get(cache.getComicId()),
                            userMap.get(cache.getUserId())))
                    .collect(Collectors.toList());

            allLogs.addAll(logs);
        }

        // 6. 벌크 인서트
        if (!allLogs.isEmpty()) {
            comicViewLogRepositoryCustom.bulkInsert(allLogs);
            log.info("Aggregated {} view logs for {} comics",
                    allLogs.size(), comicIds.size());
        }
        return true;
    }

    /**
     * 조회 수 처리
     * 1. db 조회 - 조회 수 처리가 완료된 건인지 확인
     * 2. redis 조회 - 조회 처리가 진행 중인 건인지 확인
     * 3. 두 가지 경우가 아닌 경우 조회처리 queue에 push,
     * 4. comic 조회 수 캐싱에 + 1
     */
    @Override
    public void recordViewHistory(ComicViewRequest comicViewRequest, Comic comic) {
        String viewHash = comicViewHashManager.generateViewHash(comicViewRequest);
        // 1. DB viewHash 조회 (당일 조회 여부 확인)
        if(!comicViewLogRepository.existsByViewHashAndDate(viewHash, LocalDate.now())) {

            // 2. redis hash 조회
            if(comicViewHashManager.validateView(viewHash)) {

                // viewHash 등록
                comicViewHashManager.processViewHash(viewHash);

                // 조회수 queue push
                comicViewQueueManager.pushToQueue(ComicViewCache.builder()
                        .comicId(comicViewRequest.getComicId())
                        .userId(comicViewRequest.getUserId())
                        .viewHash(viewHash)
                        .viewedDate(LocalDate.now())
                        .deviceInfo(comicViewRequest.getDeviceInfo())
                        .ipAddress(comicViewRequest.getIpAddress())
                        .build());

                // 조회수 + 1
                ComicVo comicVo = Comic.from(comic);
                comicCacheManager.incrementViewCount(comicVo);
            }
        }
    }

    @Override
    public boolean aggregateViewStats() {
        List<ComicViewLog> viewLogs = comicViewLogRepository.findAllViewLogsByDate(LocalDate.now().minusDays(1));

        Map<GroupKey, Long> dailyCounts = viewLogs.stream()
                .collect(Collectors.groupingBy(
                        log -> new GroupKey(log.getComic(), log.getViewedAt()),
                        Collectors.counting()
                ));

        List<ComicViewStats> statsToSave = new ArrayList<>();

        for (Map.Entry<GroupKey, Long> entry : dailyCounts.entrySet()) {
            Comic comic = entry.getKey().comic();
            LocalDate statsDate = entry.getKey().date();
            Long dailyCount = entry.getValue();

            Optional<ComicViewStats> existingStats = comicViewStatsRepository
                    .findByComicIdAndStatsDate(comic.getId(), statsDate);

            ComicViewStats stats;
            if (existingStats.isPresent()) {
                stats = existingStats.get();
                stats.updateCounts(dailyCount);
            } else {
                Long totalCount = getTotalCountForComic(comic.getId(), statsDate);
                Long newTotalCount = totalCount == 0 ? dailyCount : totalCount + dailyCount;

                stats = ComicViewStats.builder()
                        .comic(comic)
                        .dailyViewCount(dailyCount)
                        .totalViewCount(newTotalCount)
                        .statsDate(statsDate)
                        .build();
            }

            statsToSave.add(stats);
        }

        comicViewStatsRepository.saveAll(statsToSave);
        return true;
    }

    private Long getTotalCountForComic(Long comicId, LocalDate currentDate) {
        return comicViewStatsRepository
                .findTopByComicIdAndStatsDateBeforeOrderByStatsDateDesc(comicId, currentDate)
                .map(ComicViewStats::getTotalViewCount)
                .orElse(0L);  // 이전 통계가 없으면 0 반환
    }

    // 그룹화를 위한 레코드 클래스
    private record GroupKey(Comic comic, LocalDate date) {}
}
