package com.service.dto.response;

import com.service.core.domain.comic.ComicVo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class GetPopularComicByPurchasesResponseDto {

    private List<ComicVo> comics;

    @Builder
    public GetPopularComicByPurchasesResponseDto(List<ComicVo> comics) {
        this.comics = comics;
    }
}
