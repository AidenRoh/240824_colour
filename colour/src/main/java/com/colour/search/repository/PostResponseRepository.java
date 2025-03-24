package com.colour.search.repository;

import com.colour.board.api.colorpalette.domain.dto.ColorDto;
import com.colour.board.api.hashtag.domain.dto.HashtagDto;
import com.colour.board.api.post.domain.dto.PostResponseDto;
import com.colour.utils.StringManipulator;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.*;
import java.util.stream.Collectors;

import static com.colour.search.domain.enums.SortOption.RELEVANCE;

@Repository
public class PostResponseRepository {

    private final NamedParameterJdbcTemplate template;

    public PostResponseRepository(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
    }

    public List<PostResponseDto> getResponses(List<Long> postIds, Pageable pageable) {
        if (postIds.isEmpty()) {
            return Collections.emptyList();
        }

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("postIds", postIds)
                .addValue("limit", pageable.getPageSize())
                .addValue("offset", pageable.getOffset());
        String relevanceOrderString = "FIELD(p.post_id, " +
                postIds.stream().map(String::valueOf).collect(Collectors.joining(",")) + ")";

        String sql = String.format("""
                SELECT p.post_id, p.title, p.views, p.likes, p.created_at
                FROM post p
                WHERE p.post_id IN (:postIds) AND p.status = 'POSTED'
                ORDER BY %s LIMIT :limit OFFSET :offset
                """, isRelevance(pageable) ? relevanceOrderString : "p." + StringManipulator.buildOrderByClause(pageable));


        return template.query(sql, params, postResponseDtoRowMapper());
    }

    public Map<Long, List<ColorDto>> getColorDtos(List<Long> postIds, Pageable pageable) {
        if (postIds.isEmpty()) {
            return Collections.emptyMap();
        }

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("postIds", postIds)
                .addValue("limit", pageable.getPageSize())
                .addValue("offset", pageable.getOffset());
        String sql = String.format("""
                SELECT pc.post_id, c.color_id, c.hex_color
                FROM color c
                JOIN post_color pc ON pc.color_id = c.color_id
                JOIN post p ON pc.post_id = p.post_id
                WHERE p.post_id IN (:postIds)
                ORDER BY %s LIMIT :limit OFFSET :offset
                """, isRelevance(pageable) ? "NULL" : "p." + StringManipulator.buildOrderByClause(pageable));

        return template.query(sql, params, rs -> {
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

    public Map<Long, List<HashtagDto>> getHashtagDtos(List<Long> postIds, Pageable pageable) {
        if (postIds.isEmpty()) {
            return Collections.emptyMap();
        }

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("postIds", postIds)
                .addValue("limit", pageable.getPageSize())
                .addValue("offset", pageable.getOffset());
        String sql = String.format("""
                SELECT ph.post_id, h.hashtag_id, h.hashtag
                FROM hashtag h
                JOIN post_hashtag ph ON ph.hashtag_id = h.hashtag_id
                JOIN post p ON ph.post_id = p.post_id
                WHERE p.post_id IN (:postIds)
                ORDER BY %s LIMIT :limit OFFSET :offset
                """, isRelevance(pageable) ? "NULL" : "p." + StringManipulator.buildOrderByClause(pageable));

        return template.query(sql, params, rs -> {
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

    private boolean isRelevance(Pageable pageable) {
        Sort sort = pageable.getSort();
        return sort.stream()
                .findFirst()
                .map(order -> order.getProperty().equals(RELEVANCE.getField()))
                .orElse(false);
    }

    private RowMapper<PostResponseDto> postResponseDtoRowMapper() {
        return BeanPropertyRowMapper.newInstance(PostResponseDto.class);
    }
}
