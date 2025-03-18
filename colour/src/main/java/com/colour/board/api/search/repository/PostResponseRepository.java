package com.colour.board.api.search.repository;

import com.colour.board.api.colorpalette.domain.dto.ColorDto;
import com.colour.board.api.hashtag.domain.dto.HashtagDto;
import com.colour.board.api.post.domain.dto.PostResponseDto;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PostResponseRepository {

    private final JdbcTemplate template;

    public PostResponseRepository(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }

    public List<PostResponseDto> getResponses(List<Long> postIds) {
        StringBuilder order = new StringBuilder();
        for (int i = 1; i <= postIds.size(); i++) {
            Long each = postIds.get(i);
            if (i > 1) {
                order.append(", ");
            }
            order.append("(").append(each).append(", ").append(i).append(")");
        }

        StringBuilder sqlPost = new StringBuilder("""
                WITH post_order(post_id, priority) AS (
                    SELECT * FROM (
                    VALUES\s
                """).append(order).append("""
                ) AS t(post_id, priority)
                )
                SELECT p.post_id, p.title, p.views, p.likes, p.created_at, p.updated_at
                FROM post p
                JOIN post_order po ON p.post_id = po.post_id
                WHERE p.status = 'POSTED'
                ORDER BY po.priority
                """);

        return template.query(sqlPost.toString(), postResponseDtoRowMapper());
    }

    public Map<Long, List<ColorDto>> getColorDtos(List<Long> postIds) {
        StringBuilder order = new StringBuilder();
        for (int i = 1; i <= postIds.size(); i++) {
            Long each = postIds.get(i);
            if (i > 1) {
                order.append(", ");
            }
            order.append(each);
        }

        StringBuilder sqlColor = new StringBuilder("""
                SELECT pc.post_id, c.color_id, c.hex_color
                FROM color c
                INNER JOIN post_color pc ON pc.color_id = c.color_id
                WHERE pc.post_id IN""").append("(").append(order).append(")")
                .append("ORDER BY NULL");

        return template.query(sqlColor.toString(), rs -> {
            Map<Long, List<ColorDto>> colorMap = new HashMap<>();
            while (rs.next()) {
                Long postId = rs.getLong("post_id");
                ColorDto colorDto = new ColorDto();
                colorDto.setColorId(rs.getLong("color_id"));
                colorDto.setHexColor(rs.getString("hex_color"));
                colorMap.computeIfAbsent(postId, k -> new ArrayList<>()).add(colorDto);
            }
            return colorMap;
        });
    }

    public Map<Long, List<HashtagDto>> getHashtagDtos(List<Long> postIds) {
        StringBuilder order = new StringBuilder();
        for (int i = 1; i <= postIds.size(); i++) {
            Long each = postIds.get(i);
            if (i > 1) {
                order.append(", ");
            }
            order.append(each);
        }

        StringBuilder sqlHashtag = new StringBuilder("""
                SELECT ph.post_id, h.hashtag_id, h.hashtag
                FROM hashtag h
                INNER JOIN post_hashtag ph ON ph.hashtag_id = h.hashtag_id
                WHERE ph.post_id IN""").append("(").append(order).append(")")
                .append("ORDER BY NULL");

        return template.query(sqlHashtag.toString(), rs -> {
            Map<Long, List<HashtagDto>> hashtagMap = new HashMap<>();
            while (rs.next()) {
                Long postId = rs.getLong("post_id");
                HashtagDto hashtagDto = new HashtagDto();
                hashtagDto.setHashtagId(rs.getLong("hashtag_id"));
                hashtagDto.setHashtag(rs.getString("hashtag"));
                hashtagMap.computeIfAbsent(postId, k -> new ArrayList<>()).add(hashtagDto);
            }
            return hashtagMap;
        });
    }

    private RowMapper<PostResponseDto> postResponseDtoRowMapper() {
        return BeanPropertyRowMapper.newInstance(PostResponseDto.class);
    }
}
