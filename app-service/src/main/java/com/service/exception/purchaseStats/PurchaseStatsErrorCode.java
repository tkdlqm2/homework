package com.service.exception.purchaseStats;

import com.service.core.exception.CommonErrorCodeType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum PurchaseStatsErrorCode implements CommonErrorCodeType {
    NOT_FOUND_PURCHASE_STATS("NOT_FOUND_PURCHASE_STATS", "구매 통계를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    INVALID_COMIC_KEY("INVALID_COMIC_KEY", "유효하지 않은 만화 키입니다.", HttpStatus.BAD_REQUEST),
    INVALID_PURCHASE_COUNT("INVALID_PURCHASE_COUNT", "유효하지 않은 구매수입니다.", HttpStatus.BAD_REQUEST),
    STATS_PROCESSING_ERROR("STATS_PROCESSING_ERROR", "통계 처리 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String errorCode;
    private final String message;
    private final HttpStatus httpStatus;
}

