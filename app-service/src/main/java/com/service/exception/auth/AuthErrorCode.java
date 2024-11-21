package com.service.exception.auth;

import com.service.core.exception.CommonErrorCodeType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AuthErrorCode implements CommonErrorCodeType {

    DUPLICATE_USER_ID("DUPLICATE_USER_ID","이미 존재하는 사용자 ID 입니다.", HttpStatus.BAD_REQUEST),
    INVALID_ACCESS_TOKEN("INVALID_ACCESS_TOKEN", "유효하지 않은 엑세스 토큰입니다.", HttpStatus.UNAUTHORIZED),
    ACCESS_TOKEN_EXPIRED("ACCESS_TOKEN_EXPIRED", "엑세스 토큰의 유효기간이 만료되었습니다.", HttpStatus.UNAUTHORIZED),
    NOT_FOUND_AUTHENTICATION("NOT_FOUND_AUTHENTICATION","인증 정보를 찾을 수 없습니다.", HttpStatus.UNAUTHORIZED),
    WRONG_PASSWORD("WRONG_PASSWORD","비밀번호를 잘못 입력하였습니다.", HttpStatus.UNAUTHORIZED),
    BAD_TOKEN("BAD_TOKEN", "잘못된 토큰 값입니다.", HttpStatus.UNAUTHORIZED),
    EMPTY_TOKEN("EMPTY_TOKEN", "토큰 값이 비어있습니다.", HttpStatus.UNAUTHORIZED),
    OTP_VALIDATION_FAIL("OTP_VALIDATION_FAIL", "OTP 인증이 실패 되었습니다.", HttpStatus.BAD_REQUEST),
    OTP_REGISTER_FAIL("OTP_REGISTER_FAIL", "OTP 인증이 실패 되었습니다. OTP 등록을 먼저 해주세요.", HttpStatus.BAD_REQUEST),
    OTP_COUNT_OVER("OTP_COUNT_OVER", "OTP 입력 횟수 초과입니다. 관리자에게 문의하세요", HttpStatus.FORBIDDEN),
    NOT_FOUND_AUTHORITY("NOT_FOUND_AUTHORITY","존재하지 않는 권한 입니다. 먼저 로그인해주세요", HttpStatus.FORBIDDEN);

    private final String errorCode;
    private final String message;
    private final HttpStatus httpStatus;
}