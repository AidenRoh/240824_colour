package com.colour.member.config;

import com.colour.member.domain.MemberMapper;
import com.colour.member.repository.MemberJdbcRepository;
import com.colour.member.repository.MemberRepository;
import com.colour.member.service.MemberService;
import com.colour.member.service.MemberServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class MemberConfig {

    private final DataSource dataSource;

    @Bean
    public MemberService memberService(MemberMapper memberMapperImpl, PasswordEncoder passwordEncoder) {
        return new MemberServiceImpl(memberRepository(), memberMapperImpl, passwordEncoder);
    }

    @Bean
    public MemberRepository memberRepository() {
        return new MemberJdbcRepository(dataSource);
    }
}
