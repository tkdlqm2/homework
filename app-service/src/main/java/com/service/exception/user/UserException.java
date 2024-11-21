package com.service.exception.user;

import com.service.core.exception.CommonErrorCodeType;
import lombok.Getter;

@Getter
public class UserException extends RuntimeException {
    private final CommonErrorCodeType commonErrorCodeType;

    public UserException(CommonErrorCodeType commonErrorCodeType) {
        super(commonErrorCodeType.getMessage());
        this.commonErrorCodeType = commonErrorCodeType;
    }
}
