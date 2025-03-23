package com.colour.member.api.users.repository;

import com.colour.board.api.colorpalette.domain.dto.ColorDto;
import com.colour.board.api.hashtag.domain.dto.HashtagDto;
import com.colour.board.api.post.domain.dto.PostResponseDto;
import com.colour.member.api.follow.domain.dto.FollowDto;
import com.colour.member.api.users.dto.UsersResponseDto;
import com.colour.utils.StringConverter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class UsersJdbcRepository implements UsersRepository {

    private final NamedParameterJdbcTemplate template;

    public UsersJdbcRepository(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public UsersResponseDto getCountableInfo(Long memberId) {
        String sql = """
                SELECT M.username,
                       COUNT(F1.followee_id) AS followees,
                       COUNT(F2.follower_id) AS followers,
                       COUNT(L.member_id) AS totalLikes
                FROM member M
                LEFT JOIN follow F1 ON M.member_id = F1.follower_id
                LEFT JOIN follow F2 ON M.member_id = F2.followee_id
                LEFT JOIN likes L ON M.member_id = L.member_id
                WHERE M.member_id = :memberId
                GROUP BY M.member_id;
                """;
        Map<String, Object> param = Map.of("memberId", memberId);
        return template.queryForObject(sql, param, myPageResponseDtoRowMapper());
    }

    @Override
    public List<PostResponseDto> getSummaryPosts(Long memberId) {
        String sql = """
                SELECT post_id, title, likes, created_at
                FROM post
                WHERE member_id = :memberId
                ORDER BY created_at DESC LIMIT 5 OFFSET 0;
                """;
        Map<String, Object> param = Map.of("memberId", memberId);
        return template.query(sql, param, postResponseDtoRowMapper());
    }

    @Override
    public List<HashtagDto> getSummaryHashtags(Long memberId) {
        String sql = """
                SELECT DISTINCT H.hashtag_id, H.hashtag
                FROM hashtag H
                JOIN post_hashtag PH ON H.hashtag_id = PH.hashtag_id
                JOIN post P ON PH.post_id = P.post_id
                WHERE P.member_id = :memberId
                ORDER BY H.hashtag LIMIT 5 OFFSET 0;
                """;
        Map<String, Object> param = Map.of("memberId", memberId);
        return template.query(sql, param, hashtagDtoRowMapper());
    }


    @Override
    public List<ColorDto> getSummaryColors(Long memberId) {
        String sql = """
                SELECT DISTINCT C.color_id, C.hex_color
                FROM color C
                JOIN post_color PC ON C.color_id = PC.color_id
                JOIN post P ON PC.post_id = P.post_id
                WHERE P.member_id = :memberId
                ORDER BY C.hex_color LIMIT 5 OFFSET 0;
                """;
        Map<String, Object> param = Map.of("memberId", memberId);
        return template.query(sql, param, colorDtoRowMapper());
    }

    @Override
    public Page<PostResponseDto> getPosts(Long memberId, Pageable pageable) {
        String sql = String.format("""
                SELECT post_id, title, views, likes, created_at, COUNT(*) OVER() AS total
                FROM post WHERE member_id = :memberId
                ORDER BY %s
                LIMIT :limit OFFSET :offset
                """, StringConverter.getSortString(pageable));
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("memberId", memberId)
                .addValue("limit", pageable.getPageSize())
                .addValue("offset", pageable.getOffset());

        AtomicInteger total = new AtomicInteger();
        List<PostResponseDto> posts = template.query(sql, params, rs -> {
            List<PostResponseDto> list = new ArrayList<>();
            boolean executeOnce = false;
            while (rs.next()) {
                if (!executeOnce) {
                    total.set(rs.getInt("total"));
                    executeOnce = true;
                }
                PostResponseDto post = new PostResponseDto();
                post.setPostId(rs.getLong("post_id"));
                post.setTitle(rs.getString("title"));
                post.setViews(rs.getLong("views"));
                post.setLikes(rs.getLong("likes"));
                post.setCreatedAt(rs.getTimestamp("created_at"));
                list.add(post);
            }
            return list;
        });
        return new PageImpl<>(posts, pageable, total.get());
    }

    @Override
    public Page<HashtagDto> getHashtags(Long memberId, Pageable pageable) {
        String sql = """
                WITH total_count AS (
                    SELECT COUNT(DISTINCT H.hashtag_id) AS total
                    FROM hashtag H
                    JOIN post_hashtag PH ON H.hashtag_id = PH.hashtag_id
                    JOIN post P ON PH.post_id = P.post_id
                    WHERE P.member_id = :memberId
                )
                SELECT H.hashtag_id, H.hashtag, tc.total AS total
                FROM hashtag H
                JOIN post_hashtag PH ON H.hashtag_id = PH.hashtag_id
                JOIN post P ON PH.post_id = P.post_id
                JOIN total_count tc ON 1=1
                WHERE P.member_id = :memberId
                ORDER BY H.hashtag LIMIT :limit OFFSET :offset;
                """;
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("memberId", memberId)
                .addValue("limit", pageable.getPageSize())
                .addValue("offset", pageable.getOffset());
        AtomicInteger total = new AtomicInteger();
        List<HashtagDto> hashtags = template.query(sql, params, rs -> {
            List<HashtagDto> list = new ArrayList<>();
            boolean executeOnce = false;
            while (rs.next()) {
                if (!executeOnce) {
                    total.set(rs.getInt("total"));
                    executeOnce = true;
                }
                HashtagDto hashtag = new HashtagDto();
                hashtag.setHashtagId(rs.getLong("hashtag_id"));
                hashtag.setHashtag(rs.getString("hashtag"));
                list.add(hashtag);
            }
            return list;
        });
        return new PageImpl<>(hashtags, pageable, total.get());
    }

    //TODO:: 추후에 MemberDetails 만든 후 FollowDto -> MemberDetailsDto
    @Override
    public Page<FollowDto> getFollowers(Long memberId, Pageable pageable) {
        String sql = """
                SELECT follower_id, followee_id, COUNT(*) OVER() AS total FROM follow
                WHERE followee_id = :memberId
                ORDER BY follower_id LIMIT :limit OFFSET :offset
                """;
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("memberId", memberId)
                .addValue("limit", pageable.getPageSize())
                .addValue("offset", pageable.getOffset());
        AtomicInteger total = new AtomicInteger();
        List<FollowDto> followers = template.query(sql, params, rs -> {
            List<FollowDto> list = new ArrayList<>();
            boolean executeOnce = false;
            while (rs.next()) {
                if (!executeOnce) {
                    total.set(rs.getInt("total"));
                    executeOnce = true;
                }
                FollowDto follower = new FollowDto();
                follower.setFollowerId(rs.getLong("follower_id"));
                follower.setFolloweeId(rs.getLong("followee_id"));
                list.add(follower);
            }
            return list;
        });
        return new PageImpl<>(followers, pageable, total.get());
    }

    //TODO:: 추후에 MemberDetails 만든 후 FollowDto -> MemberDetailsDto
    @Override
    public Page<FollowDto> getFollowees(Long memberId, Pageable pageable) {
        String sql = """
                SELECT follower_id, followee_id, COUNT(*) OVER() AS total FROM follow
                WHERE follower_id = :memberId
                ORDER BY followee_id LIMIT :limit OFFSET :offset
                """;
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("memberId", memberId)
                .addValue("limit", pageable.getPageSize())
                .addValue("offset", pageable.getOffset());
        AtomicInteger total = new AtomicInteger();
        List<FollowDto> followers = template.query(sql, params, rs -> {
            List<FollowDto> list = new ArrayList<>();
            boolean executeOnce = false;
            while (rs.next()) {
                if (!executeOnce) {
                    total.set(rs.getInt("total"));
                    executeOnce = true;
                }
                FollowDto follower = new FollowDto();
                follower.setFollowerId(rs.getLong("follower_id"));
                follower.setFolloweeId(rs.getLong("followee_id"));
                list.add(follower);
            }
            return list;
        });
        return new PageImpl<>(followers, pageable, total.get());
    }

    private RowMapper<UsersResponseDto> myPageResponseDtoRowMapper() {
        return BeanPropertyRowMapper.newInstance(UsersResponseDto.class);
    }

    private RowMapper<HashtagDto> hashtagDtoRowMapper() {
        return BeanPropertyRowMapper.newInstance(HashtagDto.class);
    }

    private RowMapper<ColorDto> colorDtoRowMapper() {
        return BeanPropertyRowMapper.newInstance(ColorDto.class);
    }

    private RowMapper<PostResponseDto> postResponseDtoRowMapper() {
        return BeanPropertyRowMapper.newInstance(PostResponseDto.class);
    }

}
