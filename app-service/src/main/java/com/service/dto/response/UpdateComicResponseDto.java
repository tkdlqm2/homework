package com.service.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateComicResponseDto {
    private Long id;

    @Builder
    public UpdateComicResponseDto(Long id) {
        this.id = id;
    }
}
