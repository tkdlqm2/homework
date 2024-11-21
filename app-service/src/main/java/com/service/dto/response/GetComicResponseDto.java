package com.service.dto.response;

import com.service.core.enums.AgeRating;
import com.service.core.enums.ComicStatus;
import com.service.core.enums.Genre;
import com.service.core.enums.PublicationDay;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@NoArgsConstructor
public class GetComicResponseDto {
    private Long id;
    private String productKey;
    private String title;
    private String description;
    private String authorName;
    private PublicationDay publicationDay;
    private ComicStatus status;
    private Set<Genre> genres;
    private Integer totalEpisodes;
    private AgeRating ageRating;
    private BigDecimal price;
    private boolean event;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public GetComicResponseDto(Long id, String productKey, String title, String description, String authorName, PublicationDay publicationDay, ComicStatus status, Set<Genre> genres, Integer totalEpisodes, AgeRating ageRating, BigDecimal price, boolean event, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.productKey = productKey;
        this.title = title;
        this.description = description;
        this.authorName = authorName;
        this.publicationDay = publicationDay;
        this.status = status;
        this.genres = genres;
        this.totalEpisodes = totalEpisodes;
        this.ageRating = ageRating;
        this.price = price;
        this.event = event;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
