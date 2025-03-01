package com.colour.member.api.member.domain.entity;

import com.colour.security.config.enums.MemberRole;
import lombok.Data;

@Data
public class Member {

    private Long memberId;
    private String username;
    private String password;
    private String email;
    private String role;

    public Member(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = MemberRole.USER.getRole();
    }

    public Member() {
        this.role = MemberRole.USER.getRole();
    }
}
