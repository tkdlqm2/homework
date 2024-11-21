package com.service.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DeleteComicResponseDto {
    private Long comicId;

    @Builder
    public DeleteComicResponseDto(Long comicId) {
        this.comicId = comicId;
    }
}
