package com.colour.follow.repository;

import com.colour.follow.domain.entity.Follow;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;
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
    public void follow(Follow follow) {
        SqlParameterSource source = new BeanPropertySqlParameterSource(follow);
        insert.execute(source);
    }

    @Override
    public void unfollow(long followerId, long followeeId) {
        String sql = "DELETE FROM follow WHERE follower_id = :followerId AND followee_id = :followeeId";
        Map<String, Object> params = Map.of("followerId", followerId, "followeeId", followeeId);
        template.update(sql, params);
    }
}
