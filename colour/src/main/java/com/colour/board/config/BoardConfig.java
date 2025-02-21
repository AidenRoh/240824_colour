package com.colour.board.config;

import com.colour.board.api.hashtag.repository.HashtagJdbcRepository;
import com.colour.board.api.hashtag.repository.HashtagRepository;
import com.colour.board.api.hashtag.service.HashtagService;
import com.colour.board.api.hashtag.service.HashtagServiceImpl;
import com.colour.board.api.likes.repository.LikesJdbcRepository;
import com.colour.board.api.likes.repository.LikesRepository;
import com.colour.board.api.likes.service.LikesService;
import com.colour.board.api.likes.service.LikesServiceImpl;
import com.colour.board.api.post.repository.PostJdbcRepository;
import com.colour.board.api.post.repository.PostRepository;
import com.colour.board.api.post.service.PostService;
import com.colour.board.api.post.service.PostServiceImpl;
import com.colour.board.api.posthashtag.repository.PostHashtagJdbcRepository;
import com.colour.board.api.posthashtag.repository.PostHashtagRepository;
import com.colour.board.api.posthashtag.service.PostHashtagService;
import com.colour.board.api.posthashtag.service.PostHashtagServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class BoardConfig {

    private final DataSource dataSource;

    @Bean
    public PostService postService() {
        return new PostServiceImpl(postRepository());
    }

    @Bean
    public PostRepository postRepository() {
        return new PostJdbcRepository(dataSource);
    }

    @Bean
    public HashtagService hashtagService() {
        return new HashtagServiceImpl(hashtagRepository());
    }

    @Bean
    public HashtagRepository hashtagRepository() {
        return new HashtagJdbcRepository(dataSource);
    }

    @Bean
    public PostHashtagService postHashtagService() {
        return new PostHashtagServiceImpl(postHashtagRepository());
    }

    @Bean
    public PostHashtagRepository postHashtagRepository() {
        return new PostHashtagJdbcRepository(dataSource);
    }

    @Bean
    public LikesService likesService() {
        return new LikesServiceImpl(likesRepository(), postRepository());
    }

    @Bean
    public LikesRepository likesRepository() {
        return new LikesJdbcRepository(dataSource);
    }
}
