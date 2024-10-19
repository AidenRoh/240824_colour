package com.colour.session.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash(value = "email", timeToLive = 120)
@RequiredArgsConstructor
public class EmailSession {

    @Id
    private final String email;
    private final String username;
    private final String password;
    private final String authCode;

    @Override
    public String toString() {
        return "EmailSession{" +
                "email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", authCode='" + authCode + '\'' +
                '}';
    }
}
