package com.service.core.enums;

import lombok.Getter;

@Getter
public enum ComicStatus {
    ONGOING("연재중"),
    COMPLETED("완결"),
    HIATUS("휴재");

    private final String description;

    ComicStatus(String description) {
        this.description = description;
    }
}