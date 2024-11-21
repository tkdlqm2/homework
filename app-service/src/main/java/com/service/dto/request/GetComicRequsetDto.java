package com.service.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetComicRequsetDto {
    @NotNull
    private Long comicId;

    @NotNull
    private Long userId;

    @Builder
    public GetComicRequsetDto(Long comicId, Long userId) {
        this.userId = userId;
        this.comicId = comicId;
    }
}
