package com.service.exception.comicViewLog;

import com.service.core.exception.CommonErrorCodeType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ComicViewLogErrorCode implements CommonErrorCodeType {
    // 조회 관련
    NOT_FOUND_VIEW_LOG("NOT_FOUND_VIEW_LOG", "조회 로그를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

    // 생성/검증 관련
    DUPLICATE_VIEW_LOG("DUPLICATE_VIEW_LOG", "동일한 조건의 조회 로그가 이미 존재합니다.", HttpStatus.BAD_REQUEST),
    INVALID_VIEW_DURATION("INVALID_VIEW_DURATION", "유효하지 않은 조회 시간입니다.", HttpStatus.BAD_REQUEST),
    INVALID_DEVICE_INFO("INVALID_DEVICE_INFO", "유효하지 않은 디바이스 정보입니다.", HttpStatus.BAD_REQUEST),
    INVALID_IP_ADDRESS("INVALID_IP_ADDRESS", "유효하지 않은 IP 주소입니다.", HttpStatus.BAD_REQUEST),

    // 데이터 처리 관련
    VIEW_LOG_PROCESSING_ERROR("VIEW_LOG_PROCESSING_ERROR", "조회 로그 처리 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String errorCode;
    private final String message;
    private final HttpStatus httpStatus;
}
