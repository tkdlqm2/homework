package com.service.dto.request;

import com.service.core.enums.AgeRating;
import com.service.core.enums.Genre;
import com.service.core.enums.PublicationDay;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@NoArgsConstructor
public class CreateComicRequestDto {
    @NotBlank
    private String title;
    private String comicKey;
    private String description;
    @NotBlank
    private String authorName;
    @NotNull
    private PublicationDay publicationDay;
    @NotEmpty
    private Set<Genre> genres;
    @NotNull
    private AgeRating ageRating;
    @NotNull
    private BigDecimal price;
    private boolean event;

    @Builder
    public CreateComicRequestDto(String title, String description, String comicKey, String authorName, PublicationDay publicationDay, Set<Genre> genres, AgeRating ageRating, BigDecimal price, boolean event) {
        this.title = title;
        this.comicKey = comicKey;
        this.description = description;
        this.authorName = authorName;
        this.publicationDay = publicationDay;
        this.genres = genres;
        this.ageRating = ageRating;
        this.price = price;
        this.event = event;
    }
}
