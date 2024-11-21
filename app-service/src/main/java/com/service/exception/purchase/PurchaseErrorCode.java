package com.service.exception.purchase;

import com.service.core.exception.CommonErrorCodeType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum PurchaseErrorCode implements CommonErrorCodeType {
    // 조회 관련
    NOT_FOUND_PURCHASE("NOT_FOUND_PURCHASE", "구매 내역을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

    // 구매 검증 관련
    DUPLICATE_PURCHASE("DUPLICATE_PURCHASE", "이미 구매한 만화입니다.", HttpStatus.BAD_REQUEST),
    INVALID_PURCHASE_PRICE("INVALID_PURCHASE_PRICE", "유효하지 않은 구매 가격입니다.", HttpStatus.BAD_REQUEST),
    INSUFFICIENT_BALANCE("INSUFFICIENT_BALANCE", "잔액이 부족합니다.", HttpStatus.BAD_REQUEST),

    // 구매 처리 관련
    PURCHASE_PROCESSING_ERROR("PURCHASE_PROCESSING_ERROR", "구매 처리 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_PURCHASE_DATE("INVALID_PURCHASE_DATE", "유효하지 않은 구매 일자입니다.", HttpStatus.BAD_REQUEST);

    private final String errorCode;
    private final String message;
    private final HttpStatus httpStatus;
}
