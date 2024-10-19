package com.colour.session.repository;

import com.colour.session.entity.EmailSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import static com.colour.session.enums.Email.*;

@Repository
public class EmailSessionRepositoryImpl implements EmailSessionRepository {

    private final HashOperations<String, String, String> operations;

    @Autowired
    public EmailSessionRepositoryImpl(RedisTemplate<String, String> redisTemplate) {
        this.operations = redisTemplate.opsForHash();
    }

    @Override
    public void createSession(EmailSession session) {
        String email = session.getEmail();
        String username = session.getUsername();
        String password = session.getPassword();
        String authCode = session.getAuthCode();
        operations.put(email, USERNAME.getValue(), username);
        operations.put(email, PASSWORD.getValue(), password);
        operations.put(email, AUTHCODE.getValue(), authCode);
    }

    @Override
    public String findAuthCode(String email) {
        return operations.get(email, AUTHCODE.getValue());
    }

    @Override
    public EmailSession findSession(String email) {
        if (operations.size(email) != 0) {
            return new EmailSession(
                    email,
                    operations.get(email, USERNAME.getValue()),
                    operations.get(email, PASSWORD.getValue()),
                    operations.get(email, AUTHCODE.getValue())
            );
        }
        return null;
    }

    @Override
    public void deleteSession(String email) {
        operations.delete(email);
    }
}
