package com.service.core.enums;

import lombok.Getter;

@Getter
public enum Genre {
    ROMANCE("로맨스"),
    ACTION("액션"),
    FANTASY("판타지"),
    DRAMA("드라마"),
    COMEDY("코미디");

    private final String description;

    Genre(String description) {
        this.description = description;
    }
}