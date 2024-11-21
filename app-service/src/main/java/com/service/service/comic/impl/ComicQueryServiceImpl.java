package com.service.service.comic.impl;

import com.service.core.domain.comic.ComicViewRequest;
import com.service.database.entity.comic.Comic;
import com.service.database.entity.comic.ComicViewLog;
import com.service.database.entity.user.User;
import com.service.database.repository.comic.ComicRepository;
import com.service.database.repository.comic.ComicViewLogRepository;
import com.service.database.repository.user.UserRepository;
import com.service.core.domain.comic.ComicVo;
import com.service.dto.request.GetComicRequsetDto;
import com.service.dto.request.GetViewLogByComicRequestDto;
import com.service.dto.response.GetComicResponseDto;
import com.service.dto.response.GetPopularComicByViewsResponseDto;
import com.service.dto.response.GetViewLogByComicResponseDto;
import com.service.exception.comic.ComicErrorCode;
import com.service.exception.comic.ComicException;
import com.service.exception.user.UserErrorCode;
import com.service.exception.user.UserException;
import com.service.service.comic.ComicQueryService;
import com.service.service.comic.ComicViewCountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ComicQueryServiceImpl implements ComicQueryService {

    private final ComicRepository comicRepository;
    private final UserRepository userRepository;
    private final ComicViewCountService comicViewCountService;
    private final ComicViewLogRepository comicViewLogRepository;

    @Override
    public GetViewLogByComicResponseDto getComicByCondition(GetViewLogByComicRequestDto getViewLogByComicRequestDto) {
        // 1. 만화 정보 조회
        Comic comic = comicRepository.findById(getViewLogByComicRequestDto.getId())
                .orElseThrow(() -> new ComicException(ComicErrorCode.NOT_FOUND_COMIC));

        List<ComicViewLog> comicViewLogs = comicViewLogRepository.findAllByComicId(comic.getId());
        // Stream을 사용한 변환
        List<GetViewLogByComicResponseDto.ComicViewVo> result = comicViewLogs.stream()
                .map(log -> GetViewLogByComicResponseDto.ComicViewVo.builder()
                        .id(log.getId())
                        .localDate(log.getViewedAt())
                        .build())
                .collect(Collectors.toList());
        return GetViewLogByComicResponseDto.builder().list(result).build();
    }

    @Override
    public GetComicResponseDto getComic(GetComicRequsetDto getComicRequsetDto) {
        log.info("ComicQueryServiceImpl.getComic function Start with : {} ", getComicRequsetDto.getComicId());

        // 1. 만화 정보 조회
        Comic comic = comicRepository.findById(getComicRequsetDto.getComicId())
                .orElseThrow(() -> new ComicException(ComicErrorCode.NOT_FOUND_COMIC));

        // 2. User 정보 조회
        User user = userRepository.findById(getComicRequsetDto.getUserId())
                .orElseThrow(() -> new UserException(UserErrorCode.NOT_FOUND_USER));

        ComicViewRequest viewRequest = ComicViewRequest.builder()
                .comicId(comic.getId())
                .userId(getComicRequsetDto.getUserId())
                .deviceInfo(user.getDeviceSerialNo())
                .ipAddress(user.getIpAddress())
                .viewDate(LocalDate.now())
                .build();

        // 조회 수 캐싱
        comicViewCountService.recordViewHistory(viewRequest, comic);

        GetComicResponseDto getComicResponseDto = GetComicResponseDto.builder()
                .event(comic.isEvent())
                .price(comic.getPrice())
                .title(comic.getTitle())
                .ageRating(comic.getAgeRating())
                .authorName(comic.getAuthorName())
                .description(comic.getDescription())
                .build();

        log.info("ComicQueryServiceImpl.getComic function End ... : {}", getComicResponseDto);
        return getComicResponseDto;
    }

    @Override
    public GetPopularComicByViewsResponseDto getPopularContentsByViews() {
        log.info("ComicQueryServiceImpl.getPopularContentsByViews function Start ... ");
        List<ComicVo> comicVos = comicViewCountService.getViewStatistics();
        log.info("ComicQueryServiceImpl.getPopularContentsByViews function End ... : {}", comicVos);
        return GetPopularComicByViewsResponseDto.builder()
                .comics(comicVos)
                .build();
    }
}
