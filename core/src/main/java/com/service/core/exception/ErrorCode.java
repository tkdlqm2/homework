package com.service.core.exception;

import com.service.core.http.HttpMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
@AllArgsConstructor
public enum ErrorCode implements HttpMessage {
    BAD_REQUEST_ERROR(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    NOT_FOUND_ERROR(HttpStatus.NOT_FOUND, "요청값이 존재하지 않습니다."),
    UNAUTHORIZED_ERROR(HttpStatus.UNAUTHORIZED, "인증 오류입니다."),
    UNKNOWN_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown server error"),
    MESSAGE_QUEUE_SENDING_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "메세지 전송 실패입니다."),
    DB_DATA_EMPTY_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "DB 데이터가 존재하지 않습니다."),
    DATA_ALREADY_IN_DB_ERROR(HttpStatus.UNPROCESSABLE_ENTITY, "이미 DB에 존재하는 데이터입니다."),
            ;

    private final HttpStatus httpStatus;
    private final String message;

    public static ErrorCode of(HttpStatus httpStatus) {
        if (httpStatus.is4xxClientError()) {
            return ErrorCode.BAD_REQUEST_ERROR;
        } else {
            return ErrorCode.UNKNOWN_SERVER_ERROR;
        }
    }
}
