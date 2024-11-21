package com.service.service.purchase.impl;

import com.service.core.domain.comic.ComicVo;
import com.service.database.entity.comic.Comic;
import com.service.database.entity.purchase.PurchaseStats;
import com.service.database.repository.comic.ComicRepository;
import com.service.database.repository.purchase.PurchaseStatsRepository;
import com.service.dto.response.GetPopularComicByPurchasesResponseDto;
import com.service.exception.purchaseStats.PurchaseStatsErrorCode;
import com.service.exception.purchaseStats.PurchaseStatsException;
import com.service.redis.cache.ComicCache;
import com.service.redis.manager.ComicPurchaseManager;
import com.service.service.purchase.PurchaseQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PurchaseQueryServiceImpl implements PurchaseQueryService {

    private final ComicPurchaseManager comicPurchaseManager;
    private final PurchaseStatsRepository purchaseStatsRepository;
    private final ComicRepository comicRepository;
    private static final int TOP_PURCHASE_COUNT = 10;

    @Override
    public GetPopularComicByPurchasesResponseDto getPopularComicsByPurchases() {
        List<ComicCache> topComics = new ArrayList<>();
        topComics = comicPurchaseManager.getTopPurchasedComics(TOP_PURCHASE_COUNT);
        // 캐싱 조회 실패
        if(topComics.isEmpty()) {
            // DB <-> Redis 동기화
            List<PurchaseStats> purchaseStats = purchaseStatsRepository.findTopNByStatsDateOrderByPurchaseCountDesc(LocalDate.now(), TOP_PURCHASE_COUNT);

            if (purchaseStats.isEmpty()) {
                log.error("No purchase stats found for date: {}", LocalDate.now());
                throw new PurchaseStatsException(PurchaseStatsErrorCode.NOT_FOUND_PURCHASE_STATS);
            }
            List<Long> comicIds = purchaseStats.stream()
                    .map(stats -> stats.getComic().getId())
                    .collect(Collectors.toList());
            List<Comic> comics = comicRepository.findByIdIn(comicIds);
            Map<Long, Comic> comicMap = comics.stream()
                    .collect(Collectors.toMap(Comic::getId, comic -> comic));

            List<Comic> orderedComics = comicIds.stream()
                    .map(comicMap::get)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            int targetSize = orderedComics.size() >= TOP_PURCHASE_COUNT ? TOP_PURCHASE_COUNT : orderedComics.size();
            List<ComicVo> comicVos = new ArrayList<>();
            for(int i = 0; i < targetSize; i++) {
                Comic target_c = comics.get(i);
                PurchaseStats target_p = purchaseStats.get(i);
                comicVos.add(Comic.from(target_c, target_p));
            }

            // 2. 캐싱 진행
            comicPurchaseManager.refreshPurchaseRanking(comicVos);
        }
        GetPopularComicByPurchasesResponseDto getPopularComicByPurchasesResponseDto = GetPopularComicByPurchasesResponseDto
                .builder()
                .comics(topComics.stream().map(ComicCache::toVo).collect(Collectors.toList()))
                .build();
        return getPopularComicByPurchasesResponseDto;
    }
}
