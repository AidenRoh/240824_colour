package com.colour.member.entity;

import com.colour.member.dto.MemberRegisterDto;
import lombok.Data;

@Data
public class Member {

    private Long memberId;
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
