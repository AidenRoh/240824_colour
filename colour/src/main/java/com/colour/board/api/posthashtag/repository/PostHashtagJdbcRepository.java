package com.colour.board.api.posthashtag.repository;

import com.colour.board.api.posthashtag.domain.dto.PostHashtagDto;
import com.colour.board.api.posthashtag.domain.entity.PostHashtag;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

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
        String sql = "SELECT * FROM tag_post WHERE";
        boolean andFlag = false;
        if (postHashtagDto.getHashtagId() != null) {
            sql += " hashtag_id = :hashtagId";
            andFlag = true;
        }
        if (postHashtagDto.getPostId() != null) {
            if (andFlag) {
                sql += " AND ";
            }
            sql += " post_id = :postId";
        }
        if (postHashtagDto.getMemberId() != null) {
            if (andFlag) {
                sql += " AND ";
            }
            sql += " member_id = :memberId";
        }
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("hashtagId", postHashtagDto.getHashtagId())
                .addValue("postId", postHashtagDto.getPostId())
                .addValue("memberId", postHashtagDto.getMemberId());
        return template.query(sql, params, tagPostRowMapper());
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
