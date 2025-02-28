package com.colour.board.api.post.repository;

import com.colour.board.api.post.domain.dto.PostDto;
import com.colour.board.api.post.domain.entity.Post;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.util.*;

import static org.springframework.util.StringUtils.hasText;

/*
 * NamedParameterJdbcTemplate
 * SqlParameterSource
 * - BeanPropertySqlParameterSource
 * - MapSqlParameterSource
 * - Map
 * - BeanPropertyRowMapper
 */
@Repository
public class PostJdbcRepository implements PostRepository {

    private NamedParameterJdbcTemplate template;
    private SimpleJdbcInsert insert;

    public PostJdbcRepository(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
        this.insert = new SimpleJdbcInsert(dataSource)
                .withTableName("post")
                .usingGeneratedKeyColumns("post_id");
    }

    @Override
    public Post save(Post post) {
        SqlParameterSource source = new BeanPropertySqlParameterSource(post);
        Number postId = insert.executeAndReturnKey(source);
        post.setPostId(postId.longValue());
        return post;
    }

    @Override
    public Optional<Post> findById(Long postId) {
        String sql = "SELECT * FROM post WHERE post_id = :id";
        try {
            Map<String, Object> param = Map.of("id", postId);
            Post post = template.queryForObject(sql, param, postRowMapper());
            assert post != null;
            return Optional.of(post);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Post> findByTitle(String title) {
        String sql = "SELECT * FROM post WHERE LOCATE(title=:title, post.title)";
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("title", title);
        return template.query(sql, param, postRowMapper());
    }

    @Override
    public void update(Long postId, PostDto dto) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder("UPDATE post SET ");
        String prefix = "";

        if (hasText(dto.getTitle())) {
            sql.append("title=:title");
            params.put("title", dto.getTitle());
            prefix = ", ";
        }

        if (hasText(dto.getContent())) {
            sql.append(prefix).append("content=:content");
            params.put("content", dto.getContent());
            prefix = ", ";
        }

        if (hasText(dto.getStatus())) {
            sql.append(prefix).append("status=:status");
            params.put("status", dto.getStatus());
            prefix = ", ";
        }

        sql.append(prefix).append("updated_at=:updatedAt WHERE post_id=:postId");
        params.put("postId", postId);
        params.put("updatedAt", new Timestamp(new Date().getTime()));
        template.update(sql.toString(), params);
    }

    @Override
    public void delete(Long postId, PostDto dto) {
        String sql = "UPDATE post SET status=:status, deleted_at=:deletedAt WHERE post_id=:id";
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("id", postId)
                .addValue("status", dto.getStatus())
                .addValue("deletedAt", new Timestamp(new Date().getTime()));
        template.update(sql, param);
    }

    @Override
    public void likePost(Long postId) {
        String sql = "UPDATE post SET likes = post.likes+1 WHERE post_id = :postId";
        Map<String, Object> param = Map.of("postId", postId);
        template.update(sql, param);
    }

    @Override
    public void dislikePost(Long postId) {
        String sql = "UPDATE post SET likes = post.likes-1 WHERE post_id = :postId";
        Map<String, Object> param = Map.of("postId", postId);
        template.update(sql, param);
    }

    @Override
    public void increaseViews(Long postId) {
        String sql = "UPDATE post SET views = post.views+1 WHERE post_id = :postId";
        Map<String, Object> param = Map.of("postId", postId);
        template.update(sql, param);
    }

    private RowMapper<Post> postRowMapper() {
        return BeanPropertyRowMapper.newInstance(Post.class);
    }
}
