package com.service.redis.exception;

import com.service.core.exception.CommonErrorCodeType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum RedisErrorCode implements CommonErrorCodeType {
    // 일반적인 Redis 작업 관련 에러
    OPERATION_FAILED("REDIS_001", "Redis 작업 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    SERIALIZATION_FAILED("REDIS_002", "데이터 직렬화 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    DESERIALIZATION_FAILED("REDIS_003", "데이터 역직렬화 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    CONNECTION_FAILED("REDIS_004", "Redis 연결 중 오류가 발생했습니다.", HttpStatus.SERVICE_UNAVAILABLE),

    // ZSet 관련 에러
    ZSET_ADD_FAILED("REDIS_101", "ZSet 추가 작업 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    ZSET_UPDATE_FAILED("REDIS_102", "ZSet 업데이트 작업 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    ZSET_REMOVE_FAILED("REDIS_103", "ZSet 삭제 작업 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    ZSET_QUERY_FAILED("REDIS_104", "ZSet 조회 작업 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),

    // 세션 관련 에러
    SESSION_SAVE_FAILED("REDIS_201", "세션 저장에 실패했습니다.",HttpStatus.INTERNAL_SERVER_ERROR),
    SESSION_NOT_FOUND("REDIS_202", "세션을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    SESSION_EXPIRED("REDIS_203", "만료된 세션입니다.", HttpStatus.UNAUTHORIZED),
    SESSION_INVALID("REDIS_204", "유효하지 않은 세션입니다.", HttpStatus.BAD_REQUEST),

    // Queue 관련 에러 코드 추가
    QUEUE_PUSH_FAILED("REDIS_301", "큐 추가 작업에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    QUEUE_POP_FAILED("REDIS_302", "큐 추출 작업에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    QUEUE_PEEK_FAILED("REDIS_303", "큐 조회 작업에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    QUEUE_SIZE_FAILED("REDIS_304", "큐 크기 조회에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    QUEUE_FULL("REDIS_305", "큐가 가득 찼습니다.", HttpStatus.TOO_MANY_REQUESTS),
    QUEUE_EMPTY("REDIS_306", "큐가 비어있습니다.", HttpStatus.NOT_FOUND),
    QUEUE_TIMEOUT("REDIS_307", "큐 작업 시간이 초과되었습니다.", HttpStatus.REQUEST_TIMEOUT);


    private final String errorCode;
    private final String message;
    private final HttpStatus httpStatus;
}
