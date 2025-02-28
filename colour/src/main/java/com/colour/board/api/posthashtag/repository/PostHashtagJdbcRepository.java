package com.colour.board.api.posthashtag.repository;

import com.colour.board.api.posthashtag.domain.dto.PostHashtagDto;
import com.colour.board.api.posthashtag.domain.entity.PostHashtag;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PostHashtagJdbcRepository implements PostHashtagRepository {

    private final NamedParameterJdbcTemplate template;
    private final SimpleJdbcInsert insert;

    public PostHashtagJdbcRepository(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
        this.insert = new SimpleJdbcInsert(dataSource)
                .withTableName("post_hashtag");
    }

    @Override
    public void save(PostHashtag postHashtag) {
        SqlParameterSource source = new BeanPropertySqlParameterSource(postHashtag);
        insert.execute(source);
    }

    @Override
    public List<PostHashtag> findByCond(PostHashtagDto postHashtagDto) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM post_hashtag WHERE 1=1");

        if (postHashtagDto.getHashtagId() != null) {
            sql.append(" AND hashtag_id = :hashtagId");
            params.put("hashtagId", postHashtagDto.getHashtagId());
        }
        if (postHashtagDto.getPostId() != null) {
            sql.append(" AND post_id = :postId");
            params.put("postId", postHashtagDto.getPostId());
        }
        if (postHashtagDto.getMemberId() != null) {
            sql.append(" AND member_id = :memberId");
            params.put("memberId", postHashtagDto.getMemberId());
        }
        return template.query(sql.toString(), params, tagPostRowMapper());
    }

    @Override
    public void delete(long postId, long hashtagId) {
        String sql = "DELETE FROM post_hashtag WHERE post_id = :postId AND hashtag_id = :hashtagId";
        Map<String, Object> param = Map.of("postId", postId, "hashtagId", hashtagId);
        template.update(sql, param);
    }

    @Override
    public boolean existsByKeys(long postId, long hashtagId) {
        String sql = "SELECT COUNT(*) FROM post_hashtag WHERE post_id = :postId AND hashtag_id = :hashtagId";
        Map<String, Object> param = Map.of("postId", postId, "hashtagId", hashtagId);
        return template.queryForObject(sql, param, Integer.class) == 1;
    }

    private RowMapper<PostHashtag> tagPostRowMapper() {
        return new BeanPropertyRowMapper<>(PostHashtag.class);
    }
}
