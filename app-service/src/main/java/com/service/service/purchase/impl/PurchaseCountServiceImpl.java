package com.service.service.purchase.impl;

import com.service.core.domain.comic.ComicVo;
import com.service.database.entity.comic.Comic;
import com.service.database.entity.purchase.Purchase;
import com.service.database.entity.purchase.PurchaseStats;
import com.service.database.repository.purchase.PurchaseRepository;
import com.service.database.repository.purchase.PurchaseStatsRepository;
import com.service.database.repository.purchase.PurchaseStatsRepositoryCustom;
import com.service.exception.purchaseStats.PurchaseStatsErrorCode;
import com.service.exception.purchaseStats.PurchaseStatsException;
import com.service.redis.exception.RedisErrorCode;
import com.service.redis.exception.RedisException;
import com.service.redis.manager.ComicPurchaseManager;
import com.service.service.purchase.PurchaseCountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PurchaseCountServiceImpl implements PurchaseCountService {

    private final ComicPurchaseManager purchaseManager;
    private final PurchaseRepository purchaseRepository;
    private final PurchaseStatsRepository purchaseStatsRepository;
    private final PurchaseStatsRepositoryCustom purchaseStatsRepositoryCustom;

    @Override
    @Transactional
    public void incrementPurchaseCount(Comic comic) {
        try {
            purchaseManager.incrementPurchaseCount(Comic.from(comic));
            log.info("Incremented purchase count for comic: {}", comic.getId());
        } catch (Exception e) {
            log.error("Failed to increment purchase count for comic: {}", comic.getId(), e);
            throw new RedisException(RedisErrorCode.ZSET_UPDATE_FAILED);
        }
    }

    @Override
    @Transactional
    public boolean aggregatePurchaseCount() {
        try {
            LocalDate today = LocalDate.now();
            List<Purchase> dailyPurchases = purchaseRepository.findAllByPurchaseDateWithComic(today);

            if (dailyPurchases.isEmpty()) {
                return true;
            }
            Map<Long, List<Purchase>> purchasesByComicId = dailyPurchases.stream()
                    .collect(Collectors.groupingBy(purchase -> purchase.getComic().getId()));


            // 3. 각 Comic별로 PurchaseStats 생성
            List<PurchaseStats> resultStats = new ArrayList<>();
            for (Map.Entry<Long, List<Purchase>> entry : purchasesByComicId.entrySet()) {
                Long comicId = entry.getKey();
                List<Purchase> purchases = entry.getValue();

                // 어제 날짜의 PurchaseStats 조회
                Long previousTotalCount = purchaseStatsRepository
                        .findByComicIdAndStatsDate(comicId, today.minusDays(1))
                        .map(PurchaseStats::getPurchaseTotalCount)
                        .orElse(0L);

                // 오늘의 구매 수와 총액 계산
                long dailyCount = purchases.size();
                BigDecimal totalAmount = purchases.stream()
                        .map(Purchase::getPrice)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                // PurchaseStats 생성
                PurchaseStats stats = PurchaseStats.builder()
                        .comic(purchases.get(0).getComic())  // 같은 comic이므로 첫 번째 것 사용
                        .purchaseCount(dailyCount)
                        .purchaseTotalCount(previousTotalCount + dailyCount)
                        .statsDate(today)
                        .totalAmount(totalAmount)
                        .build();

                resultStats.add(stats);
            }

            for(PurchaseStats stats: resultStats) {
                Optional<PurchaseStats> existingStats = purchaseStatsRepository
                        .findByComicIdAndStatsDateForPurchase(stats.getComic().getId(), stats.getStatsDate());

                if (existingStats.isPresent()) {
                    PurchaseStats existing = existingStats.get();
                    existing.updateStats(
                            stats.getPurchaseCount(),
                            stats.getPurchaseTotalCount(),
                            stats.getTotalAmount()
                    );
                    purchaseStatsRepository.save(existing);

                } else {
                    purchaseStatsRepository.save(stats);
                }

            }

            log.info("Successfully aggregated purchase stats for {} comics on {}",
                    resultStats.size(), today);
            return true;

        } catch (Exception e) {
            log.error("Failed to aggregate purchase counts", e);
            throw new PurchaseStatsException(PurchaseStatsErrorCode.STATS_PROCESSING_ERROR);
        }
    }

}
