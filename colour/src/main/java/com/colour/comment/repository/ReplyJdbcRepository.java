package com.colour.comment.repository;

import com.colour.comment.domain.dto.ResponseSearchCond;
import com.colour.comment.domain.dto.ResponseUpdateDto;
import com.colour.comment.domain.entity.Reply;
import com.colour.comment.domain.entity.Response;
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

public class ReplyJdbcRepository implements ResponseRepository {

    private final NamedParameterJdbcTemplate template;
    private final SimpleJdbcInsert insert;

    public ReplyJdbcRepository(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
        this.insert = new SimpleJdbcInsert(dataSource)
                .withTableName("reply")
                .usingGeneratedKeyColumns("reply_id");
    }

    @Override
    public Response save(Response reply) {
        SqlParameterSource source = new BeanPropertySqlParameterSource(reply);
        Number key = insert.executeAndReturnKey(source);
        ((Reply) reply).setReplyId(key.longValue());
        return reply;
    }

    @Override
    public Optional<Response> findById(Long replyId) {
        String sql = "SELECT reply_id, comment_id, member_id, writer, comment, created_at, updated_at, deleted_at FROM reply WHERE reply_id=:id";
        try {
            Map<String, Object> param = Map.of("id", replyId);
            Reply reply = template.queryForObject(sql, param, commentRowMapper());
            assert reply != null;
            return Optional.of(reply);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Response> findAll(ResponseSearchCond cond) {
        return List.of();
    }

    @Override
    public void update(Long replyId, ResponseUpdateDto dto) {
        String sql = "UPDATE reply SET comment=:comment, updated_at=:updatedAt WHERE reply_id=:id";
        SqlParameterSource source = new MapSqlParameterSource()
                .addValue("id", replyId)
                .addValue("comment", dto.getComment())
                .addValue("updatedAt", new Timestamp(new Date().getTime()));
        template.update(sql, source);
    }

    @Override
    public void delete(Long replyId) {
        String sql = "UPDATE reply SET deleted_at=:deletedAt WHERE reply_id=:id";
        SqlParameterSource source = new MapSqlParameterSource()
                .addValue("id", replyId)
                .addValue("deletedAt", new Timestamp(new Date().getTime()));
        template.update(sql, source);
    }

    private RowMapper<Reply> commentRowMapper() {
        return BeanPropertyRowMapper.newInstance(Reply.class);
    }
}
