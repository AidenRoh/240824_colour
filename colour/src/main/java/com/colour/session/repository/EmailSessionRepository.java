package com.colour.session.repository;

import com.colour.session.entity.EmailSession;

public interface EmailSessionRepository {

    void createSession(EmailSession session);

    String findAuthCode(String email);

    EmailSession findSession(String email);

    void deleteSession(String email);
}
