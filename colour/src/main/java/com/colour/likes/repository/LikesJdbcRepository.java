package com.colour.likes.repository;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

public class LikesJdbcRepository implements LikesRepository {

    private final NamedParameterJdbcTemplate template;

    public LikesJdbcRepository(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public void saveLike(long postId, long memberId) {
        String sql = "INSERT INTO likes (post_id, member_id, created_at) VALUES (:postId, :memberId, :createdAt)";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("postId", postId)
                .addValue("memberId", memberId)
                .addValue("createdAt", new Timestamp(new Date().getTime()));
        template.update(sql, params);
    }

    @Override
    public void deleteLike(long postId, long memberId) {
        String sql = "DELETE FROM likes WHERE post_id = :postId and member_id = :memberId";
        Map<String, Object> params = Map.of("postId", postId, "memberId", memberId);
        template.update(sql, params);
    }
}
