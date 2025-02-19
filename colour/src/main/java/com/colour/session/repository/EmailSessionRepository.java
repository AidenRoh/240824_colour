package com.colour.session.repository;

import com.colour.member.domain.dto.MemberRegisterDto;
import com.colour.session.entity.EmailSession;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailSessionRepository {

    void createSession(EmailSession session);

    String findAuthCode(String email);

    MemberRegisterDto getMemberRegisterDto(String email);

    void deleteSession(String email);
}
