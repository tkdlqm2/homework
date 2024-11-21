package com.service.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AggregatePurchaseCountResponseDto {

    private boolean result;

    @Builder
    public AggregatePurchaseCountResponseDto(boolean result) {
        this.result = result;
    }
}
