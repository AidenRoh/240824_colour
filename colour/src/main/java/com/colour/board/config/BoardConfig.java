package com.colour.board.config;

import com.colour.board.api.hashtag.repository.HashtagJdbcRepository;
import com.colour.board.api.hashtag.repository.HashtagRepository;
import com.colour.board.api.hashtag.service.HashtagService;
import com.colour.board.api.hashtag.service.HashtagServiceImpl;
import com.colour.board.api.post.repository.PostJdbcRepository;
import com.colour.board.api.post.repository.PostRepository;
import com.colour.board.api.post.service.PostService;
import com.colour.board.api.post.service.PostServiceImpl;
import com.colour.board.api.tagtopost.repository.TagPostJdbcRepository;
import com.colour.board.api.tagtopost.repository.TagPostRepository;
import com.colour.board.api.tagtopost.service.TagPostService;
import com.colour.board.api.tagtopost.service.TagPostServiceImpl;
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
    public TagPostService tagPostService() {
        return new TagPostServiceImpl(tagPostRepository());
    }

    @Bean
    public TagPostRepository tagPostRepository() {
        return new TagPostJdbcRepository(dataSource);
    }

}
