package com.colour.session.service;

import com.colour.member.dto.MemberRegisterDto;
import com.colour.session.entity.EmailSession;
import com.colour.session.repository.EmailSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EmailSessionServiceImpl implements EmailSessionService {

    @Autowired
    private EmailSessionRepository emailSessionRepository;

    @Override
    public void createSession(MemberRegisterDto dto, String authCode) {
        EmailSession session = new EmailSession(
                dto.getEmail(),
                dto.getUsername(),
                dto.getPassword(),
                authCode);
        emailSessionRepository.createSession(session);
    }

    @Override
    public String findAuthCode(String email) {
        return emailSessionRepository.findAuthCode(email);
    }

    @Override
    public EmailSession findSession(String email) {
        return emailSessionRepository.findSession(email);
    }

    @Override
    public void deleteSession(String email) {
        emailSessionRepository.deleteSession(email);
    }
}
