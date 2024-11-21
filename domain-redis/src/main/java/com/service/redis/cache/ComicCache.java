package com.service.redis.cache;

import com.service.core.domain.comic.ComicVo;
import com.service.core.enums.AgeRating;
import com.service.core.enums.Genre;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;

/**
 * Redis에 저장될 랭킹 정보 Value 객체
 */
@Getter
@Setter
@RedisHash("comic_cache")
public class ComicCache implements Serializable {
    private Long comicId;
    private String title;
    private String authorName;
    private Set<Genre> genres;
    private AgeRating ageRating;
    private BigDecimal price;
    private boolean event;
    private int rank;

    @Builder
    public ComicCache(Long comicId, String title, String authorName, Set<Genre> genres, AgeRating ageRating, BigDecimal price, Long viewCount, boolean event, int rank) {
        this.comicId = comicId;
        this.title = title;
        this.authorName = authorName;
        this.genres = genres;
        this.ageRating = ageRating;
        this.price = price;
        this.event = event;
        this.rank = rank;
    }

    public ComicVo toVo() {
        return ComicVo.builder()
                .title(this.title)
                .authorName(this.authorName)
                .genres(this.genres)
                .ageRating(this.ageRating)
                .price(this.price)
                .event(this.event)
                .build();
    }

}

