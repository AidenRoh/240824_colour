package com.colour.session.service;

import com.colour.member.dto.MemberRegisterDto;
import com.colour.session.entity.EmailSession;

public interface EmailSessionService {

    void createSession(MemberRegisterDto dto, String authCode);

    String findAuthCode(String email);

    EmailSession findSession(String email);

    void deleteSession(String email);
}
