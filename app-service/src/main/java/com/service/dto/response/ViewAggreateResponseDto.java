package com.service.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ViewAggreateResponseDto {

    private boolean result;

    @Builder
    public ViewAggreateResponseDto(boolean result) {
        this.result = result;
    }
}
