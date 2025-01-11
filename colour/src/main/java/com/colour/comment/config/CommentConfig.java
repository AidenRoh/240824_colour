package com.colour.comment.config;

import com.colour.comment.repository.CommentJdbcRepository;
import com.colour.comment.repository.ReplyJdbcRepository;
import com.colour.comment.repository.ResponseRepository;
import com.colour.comment.service.CommentService;
import com.colour.comment.service.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class CommentConfig {

    private final DataSource dataSource;

    @Bean
    public CommentService commentService() {
        return new CommentService(commentRepository());
    }

    @Bean
    public ResponseRepository commentRepository() {
        return new CommentJdbcRepository(dataSource);
    }

    @Bean
    public ReplyService replyService() {
        return new ReplyService(replyRepository());
    }

    @Bean
    public ResponseRepository replyRepository() {
        return new ReplyJdbcRepository(dataSource);
    }
}
