package com.colour.board.api.post.repository;

import com.colour.board.api.post.domain.dto.PostRequestDto;
import com.colour.board.api.post.domain.entity.PostContent;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Map;

@Repository
public class PostContentJdbcRepository implements PostContentRepository {

    private final NamedParameterJdbcTemplate template;
    private final SimpleJdbcInsert insert;

    public PostContentJdbcRepository(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
        this.insert = new SimpleJdbcInsert(dataSource)
                .withTableName("post_content")
                .usingGeneratedKeyColumns("content_id");
    }

    @Override
    public PostContent save(PostContent postContent) {
        SqlParameterSource params = new BeanPropertySqlParameterSource(postContent);
        Number key = insert.executeAndReturnKey(params);
        postContent.setContentId(key.longValue());
        return postContent;
    }

    @Override
    public void update(Long postId, PostRequestDto postDto) {
        String sql = "UPDATE post_content SET content = :content, updated_at = :updatedAt WHERE post_id = :postId";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("postId", postId)
                .addValue("content", postDto.getContent())
                .addValue("updated_at", postDto.getTimestamp());
        template.update(sql, params);
    }

    @Override
    public void delete(Long postId) {
        String sql = "DELETE FROM post_content WHERE post_id = :postId";
        Map<String, Object> param = Map.of("postId", postId);
        template.update(sql, param);
    }
}
