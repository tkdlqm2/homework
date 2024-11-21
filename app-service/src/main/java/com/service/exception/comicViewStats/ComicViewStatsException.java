package com.service.exception.comicViewStats;

import com.service.core.exception.CommonErrorCodeType;
import lombok.Getter;

@Getter
public class ComicViewStatsException extends RuntimeException {
    private final CommonErrorCodeType commonErrorCodeType;

    public ComicViewStatsException(CommonErrorCodeType commonErrorCodeType) {
        super(commonErrorCodeType.getMessage());
        this.commonErrorCodeType = commonErrorCodeType;
    }
}

