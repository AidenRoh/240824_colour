package com.colour.board.api.likes.config;

import com.colour.board.api.post.repository.PostRepository;
import com.colour.board.api.likes.repository.LikesJdbcRepository;
import com.colour.board.api.likes.repository.LikesRepository;
import com.colour.board.api.likes.service.LikesService;
import com.colour.board.api.likes.service.LikesServiceImpl;
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
