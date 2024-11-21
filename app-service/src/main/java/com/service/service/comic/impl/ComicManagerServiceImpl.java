package com.service.service.comic.impl;

import com.service.core.enums.ComicStatus;
import com.service.core.enums.Genre;
import com.service.database.entity.comic.Comic;
import com.service.database.entity.comic.ComicViewStats;
import com.service.database.repository.comic.ComicRepository;
import com.service.database.repository.comic.ComicViewLogRepository;
import com.service.database.repository.comic.ComicViewStatsRepository;
import com.service.dto.request.CreateComicRequestDto;
import com.service.dto.request.DeleteComicRequestDto;
import com.service.dto.request.UpdateComicRequestDto;
import com.service.dto.response.CreateComicResponseDto;
import com.service.dto.response.DeleteComicResponseDto;
import com.service.dto.response.UpdateComicResponseDto;
import com.service.exception.comic.ComicErrorCode;
import com.service.exception.comic.ComicException;
import com.service.service.comic.ComicManagerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ComicManagerServiceImpl implements ComicManagerService {

    private final ComicRepository comicRepository;
    private final ComicViewLogRepository comicViewLogRepository;
    private final ComicViewStatsRepository comicViewStatsRepository;

    @Override
    @Transactional
    public CreateComicResponseDto createComic(CreateComicRequestDto requestDto) {
        log.info("ComicManagerServiceImpl.createComic function start");
        // 1. 상품키 중복 검증
        if (comicRepository.existsByProductKey(requestDto.getComicKey())) {
            throw new ComicException(ComicErrorCode.DUPLICATE_PRODUCT_KEY);
        }

        // 2. 기본 필드 검증
        validateComicFields(requestDto);

        try {
            Comic comic = Comic.builder()
                    .productKey(requestDto.getComicKey())
                    .title(requestDto.getTitle())
                    .description(requestDto.getDescription())
                    .authorName(requestDto.getAuthorName())
                    .publicationDay(requestDto.getPublicationDay())
                    .status(ComicStatus.ONGOING)
                    .genres(new HashSet<>(requestDto.getGenres()))
                    .ageRating(requestDto.getAgeRating())
                    .price(requestDto.getPrice())
                    .event(requestDto.isEvent())
                    .build();

            Comic savedComic = comicRepository.save(comic);
            log.info("ComicManagerServiceImpl.createComic function end : {}", savedComic.getCreationId());

            return CreateComicResponseDto.builder()
                    .createdAt(savedComic.getCreationDtm())
                    .title(savedComic.getTitle())
                    .id(savedComic.getId())
                    .productKey(savedComic.getProductKey())
                    .build();

        } catch (Exception e) {
            log.error("Failed to create comic: {}", requestDto, e);
            throw e;
        }
    }

    @Override
    @Transactional
    public DeleteComicResponseDto deleteComic(DeleteComicRequestDto requestDto) {
        log.info("ComicManagerServiceImpl.deleteComic function start with : {}", requestDto);
        Comic comic = comicRepository.findById(requestDto.getComicId())
                .orElseThrow(() -> new ComicException(ComicErrorCode.NOT_FOUND_PRODUCT_KEY));

        try {
            int BATCH_SIZE = 1000;
            // 1. 대용량 로그 데이터 배치 삭제
            List<Long> logIds = comicViewLogRepository.findIdsByComicId(comic.getId());
            List<List<Long>> batches = partition(logIds, BATCH_SIZE);

            for (List<Long> batch : batches) {
                comicViewLogRepository.deleteAllByIds(batch);
                log.info("Deleted {} view logs for comic id: {}", batch.size(), comic.getId());
            }

            // 2. 통계 데이터 삭제
            comicViewStatsRepository.deleteAllByComicId(comic.getId());
            log.info("Deleted view stats for comic id: {}", comic.getId());

            // 3. Comic 삭제
            comicRepository.delete(comic);

            log.info("Deleted comic id: {}", comic.getId());

            return DeleteComicResponseDto.builder()
                    .comicId(comic.getId())
                    .build();

        } catch (Exception e) {
            log.error("Failed to delete comic: {}", requestDto, e);
            throw e;
        }
    }

    @Override
    @Transactional
    public UpdateComicResponseDto updateComicStatus(UpdateComicRequestDto requestDto) {
        log.info("ComicManagerServiceImpl.updateComicStatus function start with : {}", requestDto);
        Comic comic = comicRepository.findByProductKey(requestDto.getComicKey())
                .orElseThrow(() -> new ComicException(ComicErrorCode.NOT_FOUND_PRODUCT_KEY));
        comic.convertToEvent();
        Comic savedComic = comicRepository.save(comic);
        log.info("ComicManagerServiceImpl.updateComicStatus function end with : {}", savedComic);
        return UpdateComicResponseDto.builder().id(savedComic.getId()).build();
    }

    private void validateComicFields(CreateComicRequestDto requestDto) {
        // 제목 검증
        if (StringUtils.isEmpty(requestDto.getTitle()) || requestDto.getTitle().length() > 100) {
            throw new ComicException(ComicErrorCode.INVALID_TITLE);
        }

        // 가격 검증
        if (requestDto.getPrice() != null && requestDto.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new ComicException(ComicErrorCode.INVALID_PRICE);
        }

        // 연령 등급 검증
        if (requestDto.getAgeRating() == null) {
            throw new ComicException(ComicErrorCode.INVALID_AGE_RATING);
        }

        // 장르 검증
        if (requestDto.getGenres() == null || requestDto.getGenres().isEmpty()) {
            throw new ComicException(ComicErrorCode.EMPTY_GENRE);
        }

        requestDto.getGenres().forEach(genre -> {
            if (!Arrays.asList(Genre.values()).contains(genre)) {
                throw new ComicException(ComicErrorCode.INVALID_GENRE);
            }
        });
    }

    private <T> List<List<T>> partition(List<T> list, int size) {
        if (list == null || list.isEmpty() || size <= 0) {
            return Collections.emptyList();
        }

        List<List<T>> batches = new ArrayList<>();
        for (int i = 0; i < list.size(); i += size) {
            batches.add(list.subList(i, Math.min(i + size, list.size())));
        }
        return batches;
    }
}
