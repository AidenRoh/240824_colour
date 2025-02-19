package com.colour.likes.config;

import com.colour.board.post.repository.PostRepository;
import com.colour.likes.repository.LikesJdbcRepository;
import com.colour.likes.repository.LikesRepository;
import com.colour.likes.service.LikesService;
import com.colour.likes.service.LikesServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class LikesConfig {

    private final DataSource dataSource;

    @Bean
    public LikesRepository likesRepository() {
        return new LikesJdbcRepository(dataSource);
    }

    @Bean
    public LikesService likesService(PostRepository postRepository) {
        return new LikesServiceImpl(likesRepository(), postRepository);
    }
}
