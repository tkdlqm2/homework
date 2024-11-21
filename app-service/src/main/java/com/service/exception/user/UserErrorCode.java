package com.service.exception.user;

import com.service.core.exception.CommonErrorCodeType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UserErrorCode implements CommonErrorCodeType {
    NOT_FOUND_USER("NOT_FOUND_USER", "사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    DUPLICATE_EMAIL("DUPLICATE_EMAIL", "이미 사용중인 이메일입니다.", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD("INVALID_PASSWORD", "비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
    INVALID_EMAIL_FORMAT("INVALID_EMAIL_FORMAT", "유효하지 않은 이메일 형식입니다.", HttpStatus.BAD_REQUEST),
    INACTIVE_USER("INACTIVE_USER", "비활성화된 사용자입니다.", HttpStatus.FORBIDDEN),
    PASSWORD_ENCRYPTION_ERROR("PASSWORD_ENCRYPTION_ERROR", "비밀번호 암호화 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_LOGIN_ATTEMPT("INVALID_LOGIN_ATTEMPT", "로그인 시도 중 오류가 발생했습니다.", HttpStatus.BAD_REQUEST),
    DUPLICATE_LOGIN("DUPLICATE_LOGIN", "다른 기기에서 로그인되어 있습니다.", HttpStatus.CONFLICT);
    private final String errorCode;
    private final String message;
    private final HttpStatus httpStatus;
}