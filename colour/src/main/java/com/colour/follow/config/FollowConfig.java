package com.colour.follow.config;

import com.colour.follow.repository.FollowJdbcRepository;
import com.colour.follow.repository.FollowRepository;
import com.colour.follow.service.FollowService;
import com.colour.follow.service.FollowServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class FollowConfig {

    private final DataSource dataSource;

    @Bean
    public FollowRepository followRepository() {
        return new FollowJdbcRepository(dataSource);
    }

    @Bean
    public FollowService followService() {
        return new FollowServiceImpl(followRepository());
    }
}
