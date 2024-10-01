package com.colour.member.entity;

import lombok.Data;

@Data
public class Member {

    private Long id;
    private String username;
    private String password;
    private String email;

    public Member(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public Member() {}
}
