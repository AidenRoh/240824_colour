package com.colour.board.api.postcolor.repository;

import com.colour.board.api.postcolor.domain.dto.PostColorDto;
import com.colour.board.api.postcolor.domain.entity.PostColor;
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
public class PostColorJdbcRepository implements PostColorRepository {

    private final NamedParameterJdbcTemplate template;
    private final SimpleJdbcInsert insert;

    public PostColorJdbcRepository(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
        this.insert = new SimpleJdbcInsert(dataSource)
                .withTableName("post_color");
    }

    @Override
    public void save(PostColor postColor) {
        SqlParameterSource params = new BeanPropertySqlParameterSource(postColor);
        insert.execute(params);
    }

    @Override
    public List<PostColor> findByCond(PostColorDto postColorDto) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM post_color WHERE 1=1");

        if (postColorDto.getPostId() != null) {
            sql.append(" AND post_id = :postId");
            params.put("postId", postColorDto.getPostId());
        }

        if (postColorDto.getColorId() != null) {
            sql.append(" AND color_id = :colorId");
            params.put("colorId", postColorDto.getColorId());
        }

        return template.query(sql.toString(), params, postColorRowMapper());
    }

    @Override
    public void delete(Long postId, Long colorId) {
        String sql = "DELETE FROM post_color WHERE post_id=:postId AND color_id=:colorId";
        Map<String, Object> params = Map.of("postId", postId, "colorId", colorId);
        template.update(sql, params);
    }

    @Override
    public boolean existsByKeys(Long postId, Long colorId) {
        String sql = "SELECT COUNT(*) FROM post_color WHERE post_id=:postId AND color_id=:colorId";
        Map<String, Object> params = Map.of("postId", postId, "colorId", colorId);
        return template.queryForObject(sql, params, Integer.class) == 1;
    }

    private RowMapper<PostColor> postColorRowMapper() {
        return BeanPropertyRowMapper.newInstance(PostColor.class);
    }
}
