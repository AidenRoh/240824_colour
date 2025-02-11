package com.colour.session.service;

import com.colour.member.domain.dto.MemberRegisterDto;
import com.colour.session.entity.EmailSession;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EmailSessionServiceImplTest {

    @Autowired
    EmailSessionService emailSessionService;

    @Test
    void test() {
        // given
        String email = "abcd@naver.com";
        String emailFake = "abab@naver.com";
        MemberRegisterDto dto = new MemberRegisterDto();
        dto.setEmail(email);
        dto.setPassword("123456");
        dto.setUsername("userA");

        // when
        emailSessionService.createSession(dto, "123412");
        EmailSession session = emailSessionService.findSession(emailFake);

        //then
//        Assertions.assertThat(session).isNull();
        System.out.println(session.toString());
    }
}