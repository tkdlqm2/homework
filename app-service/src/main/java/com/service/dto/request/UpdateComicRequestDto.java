package com.service.dto.request;

import com.service.core.enums.AgeRating;
import com.service.core.enums.ComicStatus;
import com.service.core.enums.Genre;
import com.service.core.enums.PublicationDay;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@NoArgsConstructor
public class UpdateComicRequestDto {
    @NotNull
    private Long comicId;
    private String comicKey;
    private String title;
    private String description;
    private String authorName;
    private PublicationDay publicationDay;
    private Set<Genre> genres;
    private ComicStatus status;
    private AgeRating ageRating;
    private BigDecimal price;
    private boolean event;

    @Builder
    public UpdateComicRequestDto(String comicKey, Long comicId, String title, String description, String authorName, PublicationDay publicationDay, Set<Genre> genres, ComicStatus status, AgeRating ageRating, BigDecimal price, boolean event) {
        this.comicId = comicId;
        this.comicKey = comicKey;
        this.title = title;
        this.description = description;
        this.authorName = authorName;
        this.publicationDay = publicationDay;
        this.genres = genres;
        this.status = status;
        this.ageRating = ageRating;
        this.price = price;
        this.event = event;
    }
}
