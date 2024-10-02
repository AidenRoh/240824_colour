package com.colour.member.config;

import com.colour.member.repository.MemberRepository;
import com.colour.member.repository.MemberRepositoryJDBC;
import com.colour.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class MemberConfig {

    private final DataSource dataSource;

    @Bean
    public MemberService memberService() {
        return new MemberService(memberRepository());
    }

    @Bean
    public MemberRepository memberRepository() {
        return new MemberRepositoryJDBC(dataSource);
    }
}
