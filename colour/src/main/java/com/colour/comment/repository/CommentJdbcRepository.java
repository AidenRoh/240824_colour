package com.colour.comment.repository;

import com.colour.comment.dto.ResponseSearchCond;
import com.colour.comment.dto.ResponseUpdateDto;
import com.colour.comment.entity.Comment;
import com.colour.comment.entity.Response;
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
        String sql = "SELECT comment_id, board_id, member_id, writer, comment, created_at, updated_at, deleted_at FROM comment WHERE comment_id=:id";
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
    public List<Response> findAll(ResponseSearchCond cond) {
        return List.of();
    }

    @Override
    public void update(Long commentId, ResponseUpdateDto dto) {
        String sql = "UPDATE comment SET comment=:comment, updated_at=:updatedAt WHERE comment_id=:id";
        SqlParameterSource source = new MapSqlParameterSource()
                .addValue("id", commentId)
                .addValue("comment", dto.getComment())
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
