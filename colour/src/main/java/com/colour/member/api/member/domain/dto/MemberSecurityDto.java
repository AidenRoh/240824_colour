package com.colour.member.api.member.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberSecurityDto {
    private long memberId;
    private String username;
    private String password;
    private String email;
}
