package com.service.service.purchase;

import com.service.database.entity.comic.Comic;

public interface PurchaseCountService {
    /**
     * 구매수 증가
     */
    void incrementPurchaseCount(Comic comic);

    /**
     * 구매 집계
     */
    boolean aggregatePurchaseCount();

}
