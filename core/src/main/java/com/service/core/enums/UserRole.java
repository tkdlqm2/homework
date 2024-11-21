package com.service.core.enums;

import lombok.Getter;

@Getter
public enum UserRole {
    ROLE_USER("일반사용자"),
    ROLE_ADMIN("관리자");

    private final String description;

    UserRole(String description) {
        this.description = description;
    }
}
