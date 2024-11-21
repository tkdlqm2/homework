package com.service.exception.comicViewLog;

import com.service.core.exception.CommonErrorCodeType;
import lombok.Getter;

@Getter
public class ComicViewLogException extends RuntimeException {
    private final CommonErrorCodeType commonErrorCodeType;

    public ComicViewLogException(CommonErrorCodeType commonErrorCodeType) {
        super(commonErrorCodeType.getMessage());
        this.commonErrorCodeType = commonErrorCodeType;
    }
}
