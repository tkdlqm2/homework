package com.service.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetViewLogByComicRequestDto {

    private Long id;

    @Builder
    public GetViewLogByComicRequestDto(Long id) {
        this.id = id;
    }
}
