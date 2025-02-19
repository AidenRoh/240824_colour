package com.colour.follow.repository;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

public class FollowJdbcRepository implements FollowRepository {

    private final NamedParameterJdbcTemplate template;
    private final SimpleJdbcInsert insert;

    public FollowJdbcRepository(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
        this.insert = new SimpleJdbcInsert(dataSource)
                .withTableName("follow");
    }

    @Override
    public void follow(long followerId, long followeeId) {
        String sql = "INSERT INTO follow (follower_id, followee_id, created_at) VALUES (:followerId, :followeeId, :createdAt)";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("followerId", followerId)
                .addValue("followeeId", followeeId)
                .addValue("createdAt", new Timestamp(new Date().getTime()));
        template.update(sql, params);
    }

    @Override
    public void unfollow(long followerId, long followeeId) {
        String sql = "DELETE FROM follow WHERE follower_id = :followerId AND followee_id = :followeeId";
        Map<String, Object> params = Map.of("followerId", followerId, "followeeId", followeeId);
        template.update(sql, params);
    }
}
