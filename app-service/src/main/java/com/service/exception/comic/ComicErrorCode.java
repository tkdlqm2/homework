package com.service.exception.comic;

import com.service.core.exception.CommonErrorCodeType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ComicErrorCode implements CommonErrorCodeType {
    // 조회 관련
    NOT_FOUND_COMIC("NOT_FOUND_COMIC", "만화를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    NOT_FOUND_PRODUCT_KEY("NOT_FOUND_PRODUCT_KEY", "상품 키를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

    // 생성/수정 관련
    DUPLICATE_PRODUCT_KEY("DUPLICATE_PRODUCT_KEY", "이미 존재하는 상품 키입니다.", HttpStatus.BAD_REQUEST),
    INVALID_TITLE("INVALID_TITLE", "유효하지 않은 제목입니다.", HttpStatus.BAD_REQUEST),
    INVALID_PRICE("INVALID_PRICE", "유효하지 않은 가격입니다.", HttpStatus.BAD_REQUEST),
    INVALID_AGE_RATING("INVALID_AGE_RATING", "유효하지 않은 연령 등급입니다.", HttpStatus.BAD_REQUEST),

    // 상태 관련
    INVALID_STATUS_CHANGE("INVALID_STATUS_CHANGE", "잘못된 상태 변경입니다.", HttpStatus.BAD_REQUEST),
    ALREADY_COMPLETED("ALREADY_COMPLETED", "이미 완결된 만화입니다.", HttpStatus.BAD_REQUEST),

    // 구독 관련
    ALREADY_SUBSCRIBED("ALREADY_SUBSCRIBED", "이미 구독 중인 만화입니다.", HttpStatus.BAD_REQUEST),
    NOT_SUBSCRIBED("NOT_SUBSCRIBED", "구독하지 않은 만화입니다.", HttpStatus.BAD_REQUEST),

    // 장르 관련
    INVALID_GENRE("INVALID_GENRE", "유효하지 않은 장르입니다.", HttpStatus.BAD_REQUEST),
    EMPTY_GENRE("EMPTY_GENRE", "최소 하나의 장르를 선택해야 합니다.", HttpStatus.BAD_REQUEST),

    // 회차 관련
    INVALID_EPISODE_COUNT("INVALID_EPISODE_COUNT", "유효하지 않은 회차 수입니다.", HttpStatus.BAD_REQUEST);

    private final String errorCode;
    private final String message;
    private final HttpStatus httpStatus;
}

