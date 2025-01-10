package com.colour.board.config;

import com.colour.board.repository.BoardJdbcRepository;
import com.colour.board.repository.BoardRepository;
import com.colour.board.service.BoardService;
import com.colour.board.service.BoardServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class BoardConfig {

    private final DataSource dataSource;

    @Bean
    public BoardService boardService() {
        return new BoardServiceImpl(boardRepository());
    }

    @Bean
    public BoardRepository boardRepository() {
        return new BoardJdbcRepository(dataSource);
    }
}
