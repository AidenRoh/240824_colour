package com.colour.board.api.likes.repository;

import com.colour.board.api.likes.domain.entity.Likes;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Map;

@Repository
public class LikesJdbcRepository implements LikesRepository {

    private final NamedParameterJdbcTemplate template;
    private final SimpleJdbcInsert insert;

    public LikesJdbcRepository(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
        this.insert = new SimpleJdbcInsert(dataSource)
                .withTableName("likes");
    }

    @Override
    public void save(Likes likes) {
        SqlParameterSource params = new BeanPropertySqlParameterSource(likes);
        insert.execute(params);
    }

    @Override
    public void delete(long postId, long memberId) {
        String sql = "DELETE FROM likes WHERE post_id = :postId and member_id = :memberId";
        Map<String, Object> params = Map.of("postId", postId, "memberId", memberId);
        template.update(sql, params);
    }

    @Override
    public boolean existsByKeys(long postId, long memberId) {
        String sql = "SELECT COUNT(*) FROM likes WHERE post_id = :postId AND member_id = :memberId";
        Map<String, Object> params = Map.of("postId", postId, "memberId", memberId);
        return template.queryForObject(sql, params, Integer.class) == 1;
    }
}
