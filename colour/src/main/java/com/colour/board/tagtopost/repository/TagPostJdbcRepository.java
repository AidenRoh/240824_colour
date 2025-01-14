package com.colour.board.tagtopost.repository;

import com.colour.board.tagtopost.dto.TagPostDto;
import com.colour.board.tagtopost.entity.TagPost;
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

public class TagPostJdbcRepository  implements TagPostRepository {

    private final NamedParameterJdbcTemplate template;
    private final SimpleJdbcInsert insert;

    public TagPostJdbcRepository(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
        this.insert = new SimpleJdbcInsert(dataSource);
    }

    @Override
    public TagPost save(TagPost tagPost) {
        SqlParameterSource source = new BeanPropertySqlParameterSource(tagPost);
        Number key = insert.executeAndReturnKey(source);
        tagPost.setTagPostId(key.longValue());
        return tagPost;
    }

    @Override
    public List<TagPost> findByCond(TagPostDto tagPostDto) {
        String sql = "SELECT * FROM tag_post WHERE";
        boolean andFlag = false;
        if (tagPostDto.getTagId() != null) {
            sql += " tag_post_id = :tagPostId";
            andFlag = true;
        }
        if (tagPostDto.getPostId() != null) {
            if (andFlag) {
                sql += " AND ";
            }
            sql += " post_id = :postId";
        }
        if (tagPostDto.getMemberId() != null) {
            if (andFlag) {
                sql += " AND ";
            }
            sql += " member_id = :memberId";
        }
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("tagPostId", tagPostDto.getTagId())
                .addValue("postId", tagPostDto.getPostId())
                .addValue("memberId", tagPostDto.getMemberId());
        return template.query(sql, params, tagPostRowMapper());
    }

    @Override
    public void delete(Long tagPostId) {
        String sql = "DELETE FROM tag_post WHERE tag_post_id = :tagPostId";
        Map<String, Object> param = Map.of("tagPostId", tagPostId);
        template.update(sql, param);
    }

    @Override
    public void delete(Long postId, Long memberId) {
        String sql = "DELETE * FROM tag_post WHERE post_id=:postId AND member_id=:memberId";
        SqlParameterSource source = new MapSqlParameterSource()
                .addValue("postId", postId)
                .addValue("memberId", memberId);
        template.update(sql, source);
    }

    private RowMapper<TagPost> tagPostRowMapper() {
        return new BeanPropertyRowMapper<>(TagPost.class);
    }
}
