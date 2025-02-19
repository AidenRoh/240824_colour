package com.colour.security.config.enums;

import lombok.Getter;

@Getter
public enum MemberRole {
    ADMIN("ROLE_ADMIN"),
    MANAGER("ROLE_MANAGER"),
    USER("ROLE_USER"),
    DEACTIVATED("ROLE_DEACTIVATED");

    private final String role;

    MemberRole(String role) {
        this.role = role;
    }
}
