package com.service.exception.auth;

import com.service.core.exception.CommonErrorCodeType;
import lombok.Getter;

@Getter
public class AuthException extends RuntimeException{
    private final CommonErrorCodeType commonErrorCodeType;

    public AuthException(CommonErrorCodeType commonErrorCodeType) {
        super(commonErrorCodeType.getMessage());
        this.commonErrorCodeType = commonErrorCodeType;
    }
}

