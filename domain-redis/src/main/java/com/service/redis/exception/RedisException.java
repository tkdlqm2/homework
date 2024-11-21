package com.service.redis.exception;

import com.service.core.exception.CommonErrorCodeType;
import lombok.Getter;

@Getter
public class RedisException extends RuntimeException {
    private final CommonErrorCodeType commonErrorCodeType;

    public RedisException(CommonErrorCodeType commonErrorCodeType) {
        super(commonErrorCodeType.getMessage());
        this.commonErrorCodeType = commonErrorCodeType;
    }
}
