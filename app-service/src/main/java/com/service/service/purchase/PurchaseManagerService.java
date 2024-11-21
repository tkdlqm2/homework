package com.service.service.purchase;

import com.service.dto.request.PurchaseComicRequestDto;
import com.service.dto.response.GetPopularComicByPurchasesResponseDto;
import com.service.dto.response.PurchaseComicResponseDto;

public interface PurchaseManagerService {

    /**
     * 작품 구매 처리
     */
    PurchaseComicResponseDto purchaseComic(PurchaseComicRequestDto purchaseComicRequestDto);
}
