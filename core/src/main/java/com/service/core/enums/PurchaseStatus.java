package com.service.core.enums;

import lombok.Getter;

@Getter
public enum PurchaseStatus {
    COMPLETED("구매완료"),
    CANCELLED("취소"),
    REFUNDED("환불");

    private final String description;

    PurchaseStatus(String description) {
        this.description = description;
    }
}