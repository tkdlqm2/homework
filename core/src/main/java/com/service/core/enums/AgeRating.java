package com.service.core.enums;

import lombok.Getter;

@Getter
public enum AgeRating {
    ALL(0, "전체이용가"),
    ADULT(19, "성인");

    private final int age;
    private final String description;

    AgeRating(int age, String description) {
        this.age = age;
        this.description = description;
    }
}