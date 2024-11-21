package com.service.exception.purchaseStats;

import com.service.core.exception.CommonErrorCodeType;
import lombok.Getter;

@Getter
public class PurchaseStatsException extends RuntimeException {
    private final CommonErrorCodeType commonErrorCodeType;

    public PurchaseStatsException(CommonErrorCodeType commonErrorCodeType) {
        super(commonErrorCodeType.getMessage());
        this.commonErrorCodeType = commonErrorCodeType;
    }
}
