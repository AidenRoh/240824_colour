package com.colour.session.enums;

import lombok.Getter;

@Getter
public enum Email {
    EMAIL("email"),
    USERNAME("username"),
    PASSWORD("password"),
    AUTHCODE("authCode");

    private final String value;

    Email(String value) {
        this.value = value;
    }

}
