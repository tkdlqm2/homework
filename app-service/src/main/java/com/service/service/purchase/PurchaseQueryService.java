package com.service.service.purchase;

import com.service.dto.response.GetPopularComicByPurchasesResponseDto;

public interface PurchaseQueryService {
    /**
     * 구매 기준 인기 작품 목록 조회
     */
    GetPopularComicByPurchasesResponseDto getPopularComicsByPurchases();
}
