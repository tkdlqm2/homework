package com.service.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DeleteComicRequestDto {
    @NotNull
    private Long comicId;

    @Builder
    public DeleteComicRequestDto(Long comicId) {
        this.comicId = comicId;
    }
}
