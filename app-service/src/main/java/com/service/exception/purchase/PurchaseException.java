package com.service.exception.purchase;

import com.service.core.exception.CommonErrorCodeType;
import lombok.Getter;

@Getter
public class PurchaseException extends RuntimeException {
    private final CommonErrorCodeType commonErrorCodeType;

    public PurchaseException(CommonErrorCodeType commonErrorCodeType) {
        super(commonErrorCodeType.getMessage());
        this.commonErrorCodeType = commonErrorCodeType;
    }
}

