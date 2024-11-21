package com.service.core.domain.comic;

import com.service.core.enums.AgeRating;
import com.service.core.enums.Genre;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Set;

@Getter
public class ComicVo {
    private Long id;
    private String title;
    private String authorName;
    private Set<Genre> genres;
    private AgeRating ageRating;
    private BigDecimal price;
    private Long viewCount;
    private Long purchaseCount;
    private boolean event;

    @Builder
    public ComicVo(Long id, String title, Long purchaseCount, String authorName, Set<Genre> genres, AgeRating ageRating, BigDecimal price, Long viewCount, boolean event) {
        this.id = id;
        this.title = title;
        this.purchaseCount = purchaseCount;
        this.authorName = authorName;
        this.genres = genres;
        this.ageRating = ageRating;
        this.price = price;
        this.viewCount = viewCount;
        this.event = event;
    }
}
