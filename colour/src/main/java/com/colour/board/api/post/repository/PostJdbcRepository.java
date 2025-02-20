package com.colour.board.api.post.repository;

import com.colour.board.api.post.dto.PostDto;
import com.colour.board.api.post.entity.Post;
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
import java.util.*;

import static org.springframework.util.CollectionUtils.isEmpty;
import static org.springframework.util.StringUtils.hasText;

/*
 * NamedParameterJdbcTemplate
 * SqlParameterSource
 * - BeanPropertySqlParameterSource
 * - MapSqlParameterSource
 * - Map
 * - BeanPropertyRowMapper
 */
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
        String sql = "SELECT post_id, writer, title, content, color_palette," +
                " member_likes, created_at, updated_at, deleted_at FROM post WHERE post_id = :id";
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
        Map<String, Object> param = new HashMap<>();
        boolean andFlag = false;
        String sql = "UPDATE post SET ";

        if (hasText(dto.getTitle())) {
            sql += "title=:title";
            param.put("title", dto.getTitle());
            andFlag = true;
        }

        if (hasText(dto.getContent())) {
            if (andFlag) {
                sql += ", ";
            }
            sql += "content=:content";
            param.put("content", dto.getContent());
            andFlag = true;
        }

        if (isEmpty(dto.getColors())) {
            if (andFlag) {
                sql += ", ";
            }
            sql += "color_palette=:colorPalette";
            param.put("colorPalette", dto.getColors().toString());
            andFlag = true;
        }

        if (andFlag) {
            sql += ", ";
        }

        sql += "updated_at=:updatedAt WHERE post_id=:id";
        param.put("id", postId);
        param.put("updatedAt", new Timestamp(new Date().getTime()));
        template.update(sql, param);
    }

    @Override
    public void delete(Long postId) {
        String sql = "UPDATE post SET deleted_at=:deletedAt WHERE post_id=:id";
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("id", postId)
                .addValue("deletedAt", new Timestamp(new Date().getTime()));
        template.update(sql, param);
    }

    @Override
    public void likePost(Long postId) {
        String sql = "UPDATE post SET member_likes = member_likes+1 WHERE post_id = :postId";
        Map<String, Object> param = Map.of("postId", postId);
        template.update(sql, param);
    }

    @Override
    public void dislikePost(Long postId) {
        String sql = "UPDATE post SET member_likes = member_likes-1 WHERE post_id = :postId";
        Map<String, Object> param = Map.of("postId", postId);
        template.update(sql, param);
    }

    private RowMapper<Post> postRowMapper() {
        return BeanPropertyRowMapper.newInstance(Post.class);
    }
}
