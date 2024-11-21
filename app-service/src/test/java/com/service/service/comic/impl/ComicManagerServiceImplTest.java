package com.service.service.comic.impl;

import com.service.core.enums.AgeRating;
import com.service.core.enums.ComicStatus;
import com.service.core.enums.Genre;
import com.service.core.enums.PublicationDay;
import com.service.database.entity.comic.Comic;
import com.service.database.repository.comic.ComicRepository;
import com.service.dto.request.CreateComicRequestDto;
import com.service.dto.request.DeleteComicRequestDto;
import com.service.dto.response.CreateComicResponseDto;
import com.service.dto.response.DeleteComicResponseDto;
import com.service.exception.comic.ComicErrorCode;
import com.service.exception.comic.ComicException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ComicManagerServiceImplTest {
    @Mock
    private ComicRepository comicRepository;

    @InjectMocks
    private ComicManagerServiceImpl comicManagerService;

    @Test
    @DisplayName("만화 생성 성공")
    void createComic_Success() {
        // given
        CreateComicRequestDto requestDto = CreateComicRequestDto.builder()
                .comicKey("TEST-001")
                .title("테스트 만화")
                .description("테스트 설명")
                .authorName("작가명")
                .publicationDay(PublicationDay.MONDAY)
                .genres(Set.of(Genre.ACTION))
                .ageRating(AgeRating.ALL)
                .event(false)
                .build();

        Comic savedComic = Comic.builder()
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

        ReflectionTestUtils.setField(savedComic, "id", 1L);
        ReflectionTestUtils.setField(savedComic, "creationDtm", LocalDateTime.now());

        when(comicRepository.existsByProductKey(requestDto.getComicKey())).thenReturn(false);
        when(comicRepository.save(any(Comic.class))).thenReturn(savedComic);

        // when
        CreateComicResponseDto response = comicManagerService.createComic(requestDto);

        // then
        assertNotNull(response);
        assertEquals(savedComic.getId(), response.getId());
        assertEquals(savedComic.getProductKey(), response.getProductKey());
        assertNotNull(response.getCreatedAt());

        verify(comicRepository).existsByProductKey(requestDto.getComicKey());
        verify(comicRepository).save(any(Comic.class));
    }

    @Test
    @DisplayName("만화 생성 실패 - 중복된 상품키")
    void createComic_Failed_DuplicateProductKey() {
        // given
        CreateComicRequestDto requestDto = CreateComicRequestDto.builder()
                .comicKey("TEST-001")
                .title("테스트 만화")
                .build();

        when(comicRepository.existsByProductKey(requestDto.getComicKey())).thenReturn(true);

        // when & then
        ComicException exception = assertThrows(ComicException.class,
                () -> comicManagerService.createComic(requestDto));

        assertEquals(ComicErrorCode.DUPLICATE_PRODUCT_KEY, exception.getCommonErrorCodeType());
        verify(comicRepository).existsByProductKey(requestDto.getComicKey());
        verify(comicRepository, never()).save(any(Comic.class));
    }

    @Test
    @DisplayName("만화 삭제 성공")
    void deleteComic_Success() {
        // given
        Long comicId = 1L;
        DeleteComicRequestDto requestDto = DeleteComicRequestDto.builder()
                .comicId(comicId)
                .build();

        Comic comic = Comic.builder()
                .productKey("TEST-001")
                .title("테스트 만화")
                .build();
        ReflectionTestUtils.setField(comic, "id", comicId);

        when(comicRepository.findById(comicId)).thenReturn(Optional.of(comic));
        doNothing().when(comicRepository).delete(comic);

        // when
        DeleteComicResponseDto response = comicManagerService.deleteComic(requestDto);

        // then
        assertNotNull(response);
        assertEquals(comicId, response.getComicId());

        verify(comicRepository).findById(comicId);
        verify(comicRepository).delete(comic);
    }

    @Test
    @DisplayName("만화 삭제 실패 - 존재하지 않는 만화")
    void deleteComic_Failed_NotFound() {
        // given
        Long comicId = 1L;
        DeleteComicRequestDto requestDto = DeleteComicRequestDto.builder()
                .comicId(comicId)
                .build();

        when(comicRepository.findById(comicId)).thenReturn(Optional.empty());

        // when & then
        ComicException exception = assertThrows(ComicException.class,
                () -> comicManagerService.deleteComic(requestDto));

        assertEquals(ComicErrorCode.NOT_FOUND_PRODUCT_KEY, exception.getCommonErrorCodeType());
        verify(comicRepository).findById(comicId);
        verify(comicRepository, never()).delete(any(Comic.class));
    }

    @Test
    @DisplayName("만화 삭제 실패 - 예외 발생")
    void deleteComic_Failed_Exception() {
        // given
        Long comicId = 1L;
        DeleteComicRequestDto requestDto = DeleteComicRequestDto.builder()
                .comicId(comicId)
                .build();

        Comic comic = Comic.builder()
                .productKey("TEST-001")
                .title("테스트 만화")
                .build();
        ReflectionTestUtils.setField(comic, "id", comicId);

        when(comicRepository.findById(comicId)).thenReturn(Optional.of(comic));
        doThrow(new RuntimeException("DB Error")).when(comicRepository).delete(comic);

        // when & then
        assertThrows(RuntimeException.class,
                () -> comicManagerService.deleteComic(requestDto));

        verify(comicRepository).findById(comicId);
        verify(comicRepository).delete(comic);
    }

}