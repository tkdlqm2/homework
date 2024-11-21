package com.service.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class VerifyAdultResponseDto {
    private boolean result;

    @Builder
    public VerifyAdultResponseDto(boolean result) {
        this.result = result;
    }
}
