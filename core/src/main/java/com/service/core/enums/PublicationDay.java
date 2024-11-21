package com.service.core.enums;


import lombok.Getter;

@Getter
public enum PublicationDay {
    MONDAY("월요일"),
    TUESDAY("화요일"),
    WEDNESDAY("수요일"),
    THURSDAY("목요일"),
    FRIDAY("금요일"),
    SATURDAY("토요일"),
    SUNDAY("일요일");

    private final String description;

    PublicationDay(String description) {
        this.description = description;
    }
}