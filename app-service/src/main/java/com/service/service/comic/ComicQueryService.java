package com.service.service.comic;

import com.service.dto.request.GetComicRequsetDto;
import com.service.dto.request.GetViewLogByComicRequestDto;
import com.service.dto.response.GetComicResponseDto;
import com.service.dto.response.GetPopularComicByViewsResponseDto;
import com.service.dto.response.GetViewLogByComicResponseDto;

public interface ComicQueryService {
    /**
     * 단일 작품 상세 조회
     */
    GetComicResponseDto getComic(GetComicRequsetDto getComicRequsetDto);

    /**
     * 조회수 기준 인기 작품 목록 조회
     */
    GetPopularComicByViewsResponseDto getPopularContentsByViews();

    /**
     *
     */
    GetViewLogByComicResponseDto getComicByCondition(GetViewLogByComicRequestDto getViewLogByComicRequestDto);
}