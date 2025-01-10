package com.colour.board.repository;

import com.colour.board.dto.BoardCond;
import com.colour.board.dto.BoardUpdateDto;
import com.colour.board.entity.Board;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
/*
 * NamedParameterJdbcTemplate
 * SqlParameterSource
 * - BeanPropertySqlParameterSource
 * - MapSqlParameterSource
 * - Map
 * - BeanPropertyRowMapper
 */
public class BoardJdbcRepository implements BoardRepository {

    private NamedParameterJdbcTemplate template;
    private SimpleJdbcInsert insertBoard;

    public BoardJdbcRepository(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
        this.insertBoard = new SimpleJdbcInsert(dataSource)
                .withTableName("board")
                .usingGeneratedKeyColumns("board_id");
    }

    @Override
    public Board create(Board board) {
        SqlParameterSource source = new BeanPropertySqlParameterSource(board);
        Number boardId = insertBoard.executeAndReturnKey(source);
        board.setBoardId(boardId.longValue());
        return board;
    }

    @Override
    public Optional<Board> findById(Long boardId) {
        String sql = "SELECT board_id, writer, title, content, userLike, createdAt, updatedAt, deletedAt FROM board WHERE board_id = :id";
        try {
            Map<String, Object> param = Map.of("id", boardId);
            Board board = template.queryForObject(sql, param, boardRowMapper());
            assert board != null;
            return Optional.of(board);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Board> findAll(BoardCond boardCond) {
        return List.of();
    }

    @Override
    public void update(Long boardId, BoardUpdateDto updateDto) {
        String sql = "UPDATE board SET ";
        boolean andFlag = updateDto.getUpdatedTitle() != null && updateDto.getUpdatedContent() != null;
        if (updateDto.getUpdatedTitle() != null) sql += "title=:title";
        if (updateDto.getUpdatedContent() != null) {
            if (andFlag) {
                sql += ", ";
            }
            sql += "content=:content";
        }
        sql += ", updatedAt=:updatedAt WHERE board_id=:id";

        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("id", boardId)
                .addValue("title", updateDto.getUpdatedTitle())
                .addValue("content", updateDto.getUpdatedContent())
                .addValue("updatedAt", new Timestamp(new Date().getTime()));
        template.update(sql, param);
    }

    @Override
    public void delete(Long boardId) {
        String sql = "UPDATE board SET deletedAt=:deletedAt WHERE board_id=:id";
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("id", boardId)
                .addValue("deletedAt", new Timestamp(new Date().getTime()));
        template.update(sql, param);
    }

    private RowMapper<Board> boardRowMapper() {
        return BeanPropertyRowMapper.newInstance(Board.class);
    }
}
