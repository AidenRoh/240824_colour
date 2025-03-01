package com.colour.session.service;

import com.colour.member.api.member.domain.dto.MemberRegisterDto;

public interface EmailSessionService {

    void createSession(MemberRegisterDto dto, String authCode);

    String findAuthCode(String email);

    MemberRegisterDto getMemberRegisterDto(String email);

    void deleteSession(String email);
}
