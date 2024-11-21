package com.service.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SubscriptionStatus {
    SUBSCRIBED("구독중"),     // 구독 중
    UNSUBSCRIBED("구독취소");    // 구독 취소

    private final String description;
}
