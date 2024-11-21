package com.service.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class PurchaseComicResponseDto {
    private Long purchaseId;

    @Builder
    public PurchaseComicResponseDto(Long purchaseId) {
        this.purchaseId = purchaseId;
    }
}
