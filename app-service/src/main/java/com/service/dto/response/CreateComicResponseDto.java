package com.service.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CreateComicResponseDto {
    private Long id;
    private String productKey;
    private String title;
    private LocalDateTime createdAt;

    @Builder
    public CreateComicResponseDto(Long id, String productKey, String title, LocalDateTime createdAt) {
        this.id = id;
        this.productKey = productKey;
        this.title = title;
        this.createdAt = createdAt;
    }
}
