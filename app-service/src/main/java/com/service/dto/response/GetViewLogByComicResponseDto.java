package com.service.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
public class GetViewLogByComicResponseDto {

    private List<ComicViewVo> list;

    @Builder
    public GetViewLogByComicResponseDto(List<ComicViewVo> list) {
        this.list = list;
    }

    @Getter
    @NoArgsConstructor
    public static class ComicViewVo {
        private long id;
        private LocalDate localDate;

        @Builder
        public ComicViewVo(long id, LocalDate localDate) {
            this.id = id;
            this.localDate = localDate;
        }
    }
}
