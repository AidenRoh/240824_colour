package com.colour.board.api.posthashtag.repository;

import com.colour.board.api.posthashtag.dto.PostHashtagDto;
import com.colour.board.api.posthashtag.entity.PostHashtag;
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
                .withTableName("post_hashtag")
                .usingGeneratedKeyColumns("post_hashtag_id");
    }

    @Override
    public PostHashtag save(PostHashtag postHashtag) {
        SqlParameterSource source = new BeanPropertySqlParameterSource(postHashtag);
        Number key = insert.executeAndReturnKey(source);
        postHashtag.setPostHashtagId(key.longValue());
        return postHashtag;
    }

    @Override
    public List<PostHashtag> findByCond(PostHashtagDto postHashtagDto) {
        String sql = "SELECT * FROM tag_post WHERE";
        boolean andFlag = false;
        if (postHashtagDto.getTagId() != null) {
            sql += " post_hashtag_id = :postHashtagId";
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
                .addValue("postHashtagId", postHashtagDto.getTagId())
                .addValue("postId", postHashtagDto.getPostId())
                .addValue("memberId", postHashtagDto.getMemberId());
        return template.query(sql, params, tagPostRowMapper());
    }

    @Override
    public void delete(Long postHashtagId) {
        String sql = "DELETE FROM post_hashtag WHERE post_hashtag_id = :postHashtagId";
        Map<String, Object> param = Map.of("postHashtagId", postHashtagId);
        template.update(sql, param);
    }

    @Override
    public void delete(Long postId, Long memberId) {
        String sql = "DELETE * FROM post_hashtag WHERE post_id=:postId AND member_id=:memberId";
        SqlParameterSource source = new MapSqlParameterSource()
                .addValue("postId", postId)
                .addValue("memberId", memberId);
        template.update(sql, source);
    }

    private RowMapper<PostHashtag> tagPostRowMapper() {
        return new BeanPropertyRowMapper<>(PostHashtag.class);
    }
}
