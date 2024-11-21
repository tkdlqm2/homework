package com.service.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class VerifyAdultRequestDto {

    private Long userId;

    @Builder
    public VerifyAdultRequestDto(Long userId) {
        this.userId = userId;
    }
}
