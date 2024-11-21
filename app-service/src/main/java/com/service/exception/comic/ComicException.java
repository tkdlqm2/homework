package com.service.exception.comic;

import com.service.core.exception.CommonErrorCodeType;
import lombok.Getter;

@Getter
public class ComicException extends RuntimeException {
    private final CommonErrorCodeType commonErrorCodeType;

    public ComicException(CommonErrorCodeType commonErrorCodeType) {
        super(commonErrorCodeType.getMessage());
        this.commonErrorCodeType = commonErrorCodeType;
    }
}
