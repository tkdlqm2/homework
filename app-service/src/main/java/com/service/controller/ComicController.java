package com.service.controller;

import com.service.dto.request.*;
import com.service.dto.response.*;
import com.service.service.comic.ComicManagerService;
import com.service.service.comic.ComicQueryService;
import com.service.service.comic.ComicViewCountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Tag(name = "comic API", description = "comic API")
@RestController  // 추가
@RequestMapping("/api/v1/comic")
@RequiredArgsConstructor
@Slf4j
public class ComicController {

    private final ComicQueryService comicQueryService;
    private final ComicManagerService comicManagerService;
    private final ComicViewCountService comicViewCountService;

    @Operation(summary = "comic 생성", description = "comic 생성")
    @PostMapping("/create")
    public ResponseEntity<CreateComicResponseDto> create(@RequestBody @Valid CreateComicRequestDto request) {
        return ResponseEntity.ok(comicManagerService.createComic(request));
    }

    @Operation(summary = "단일 작품 상세 조회", description = "작품 ID로 상세 정보 조회")
    @PostMapping("/view")
    public ResponseEntity<GetComicResponseDto> getComic(@RequestBody @Valid GetComicRequsetDto getComicRequsetDto) {
        return ResponseEntity.ok(comicQueryService.getComic(getComicRequsetDto));
    }

    @Operation(summary = "인기 작품 목록 조회", description = "조회수 기준 인기 작품 목록 조회")
    @GetMapping("/popular")
    public ResponseEntity<GetPopularComicByViewsResponseDto> getPopularComics() {
        return ResponseEntity.ok(comicQueryService.getPopularContentsByViews());
    }

    @Operation(summary = "작품 삭제", description = "작품 ID로 삭제")
    @DeleteMapping
    public ResponseEntity<DeleteComicResponseDto> deleteComic(@RequestBody @Valid DeleteComicRequestDto request) {
        return ResponseEntity.ok(comicManagerService.deleteComic(request));
    }

    @Operation(summary = "작품 상태 업데이트", description = "작품 키로 상태 업데이트")
    @PutMapping("/status")
    public ResponseEntity<UpdateComicResponseDto> updateComicStatus(@RequestBody @Valid UpdateComicRequestDto request) {
        return ResponseEntity.ok(comicManagerService.updateComicStatus(request));
    }

    @Operation(summary = "조회수 집계", description = "코믹 조회수 데이터를 집계합니다")
    @GetMapping("/aggregate-views")
    public ResponseEntity<ViewAggreateResponseDto> aggregateViews() {
        return ResponseEntity.ok(ViewAggreateResponseDto.builder().result(comicViewCountService.aggregateComicViewCount()).build());
    }

    @Operation(summary = "조회수 어그리게이트 집계", description = "코믹 조회수 데이터를 집계합니다")
    @GetMapping("/aggregate-views-stats")
    public ResponseEntity<ViewAggreateResponseDto> aggregateViewsStats() {
        return ResponseEntity.ok(ViewAggreateResponseDto.builder().result(comicViewCountService.aggregateViewStats()).build());
    }

    @Operation(summary = "작품 조회 이력 ", description = "언제 어떤 사용자가 조회한지 이력")
    @PostMapping("/view-condition")
    public ResponseEntity<GetViewLogByComicResponseDto> getComicByCondition(@RequestBody @Valid GetViewLogByComicRequestDto getComicRequsetDto) {
        return ResponseEntity.ok(comicQueryService.getComicByCondition(getComicRequsetDto));
    }

}
