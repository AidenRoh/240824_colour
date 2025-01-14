package com.colour.board.post.repository;

import com.colour.board.post.dto.PostDto;
import com.colour.board.post.dto.PostSearchCond;
import com.colour.board.post.entity.Post;
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
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
    private SimpleJdbcInsert insertBoard;

    public PostJdbcRepository(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
        this.insertBoard = new SimpleJdbcInsert(dataSource)
                .withTableName("post")
                .usingGeneratedKeyColumns("post_id");
    }

    @Override
    public Post save(Post post) {
        SqlParameterSource source = new BeanPropertySqlParameterSource(post);
        Number postId = insertBoard.executeAndReturnKey(source);
        post.setPostId(postId.longValue());
        return post;
    }

    @Override
    public Optional<Post> findById(Long postId) {
        String sql = "SELECT post_id, writer, title, content, color_palette," +
                " user_like, created_at, updated_at, deleted_at FROM post WHERE post_id = :id";
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
    public List<Post> findAll(PostSearchCond postCond) {
        String sql = "SELECT * FROM post WHERE LOCATE(content=:content, post.content)";
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("content", postCond.getKeyword());
        return template.query(sql, param, postRowMapper());
    }

    @Override
    public void update(Long postId, PostDto updateDto) {
        String sql = "UPDATE post SET ";
        boolean andFlag = false;
        if (updateDto.getTitle() != null){
            sql += "title=:title";
            andFlag = true;
        }
        if (updateDto.getContent() != null) {
            if (andFlag) {
                sql += ", ";
            }
            sql += "content=:content";
            andFlag = true;
        }
        if (!updateDto.getColors().isEmpty()) {
            if (andFlag) {
                sql += ", ";
            }
            sql += "color_palette=:color_palette";
        }
        sql += ", updated_at=:updatedAt WHERE post_id=:id";

        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("id", postId)
                .addValue("title", updateDto.getTitle())
                .addValue("content", updateDto.getContent())
                .addValue("color_palette", updateDto.getColors().toString())
                .addValue("updatedAt", new Timestamp(new Date().getTime()));
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

    private RowMapper<Post> postRowMapper() {
        return BeanPropertyRowMapper.newInstance(Post.class);
    }
}
