package com.colour.member.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberRegisterDto {

    private String username;
    private String password;
    private String email;
    private Integer authNumber;

    //validation logic

    //retry: when given data is not validated, pick the inappropriate datum remove then give them back
}
