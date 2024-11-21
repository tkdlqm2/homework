package com.service.service.comic;

import com.service.core.domain.comic.ComicViewRequest;
import com.service.core.domain.comic.ComicVo;
import com.service.database.entity.comic.Comic;
import com.service.database.entity.comic.ComicViewLog;
import com.service.database.entity.comic.ComicViewStats;

import java.util.List;

public interface ComicViewCountService {

    /**
     * 조회수 통계 조회
     */
    List<ComicVo> getViewStatistics();

    /**
     * 조회수 집계
     */
    boolean aggregateComicViewCount();

    /**
     * 작품 조회 이력 기록
     */
    void recordViewHistory(ComicViewRequest comicViewRequest, Comic comic);

    /**
     * 특정 작품의 조회 이력 조회 -> 쿼리DS 페이징 처리 진행하기.
     */
//    GetComicViewHistoryResponseDto getComicViewHistory(GetComicViewHistoryRequestDto request);

    boolean aggregateViewStats();
}
