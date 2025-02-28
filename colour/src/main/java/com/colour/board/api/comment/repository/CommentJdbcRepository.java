package com.colour.board.api.comment.repository;

import com.colour.board.api.comment.domain.dto.ResponseDto;
import com.colour.board.api.comment.domain.entity.Comment;
import com.colour.board.api.comment.domain.entity.Response;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class CommentJdbcRepository implements ResponseRepository {

    private final NamedParameterJdbcTemplate template;
    private final SimpleJdbcInsert insert;

    public CommentJdbcRepository(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
        this.insert = new SimpleJdbcInsert(dataSource)
                .withTableName("comment")
                .usingGeneratedKeyColumns("comment_id");
    }

    @Override
    public Response save(Response comment) {
        SqlParameterSource source = new BeanPropertySqlParameterSource(comment);
        Number key = insert.executeAndReturnKey(source);
        ((Comment) comment).setCommentId(key.longValue());
        return comment;
    }

    @Override
    public Optional<Response> findById(Long commentId) {
        String sql = "SELECT * FROM comment WHERE comment_id=:id";
        try {
            Map<String, Object> param = Map.of("id", commentId);
            Comment comment = template.queryForObject(sql, param, commentRowMapper());
            assert comment != null;
            return Optional.of(comment);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Response> findAll(ResponseDto cond) {
        return List.of();
    }

    @Override
    public void update(Long commentId, ResponseDto dto) {
        String sql = "UPDATE comment SET content=:content, updated_at=:updatedAt WHERE comment_id=:id";
        SqlParameterSource source = new MapSqlParameterSource()
                .addValue("id", commentId)
                .addValue("content", dto.getContent())
                .addValue("updatedAt", new Timestamp(new Date().getTime()));
        template.update(sql, source);
    }

    @Override
    public void delete(Long commentId) {
        String sql = "UPDATE comment SET deleted_at=:deletedAt WHERE comment_id=:id";
        SqlParameterSource source = new MapSqlParameterSource()
                .addValue("id", commentId)
                .addValue("deletedAt", new Timestamp(new Date().getTime()));
        template.update(sql, source);
    }

    private RowMapper<Comment> commentRowMapper() {
        return BeanPropertyRowMapper.newInstance(Comment.class);
    }
}
