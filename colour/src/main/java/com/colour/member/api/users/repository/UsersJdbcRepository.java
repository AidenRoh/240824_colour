package com.colour.member.api.users.repository;

import com.colour.board.api.colorpalette.domain.dto.ColorDto;
import com.colour.board.api.hashtag.domain.dto.HashtagDto;
import com.colour.board.api.post.domain.dto.PostResponseDto;
import com.colour.member.api.follow.domain.dto.FollowDto;
import com.colour.member.api.users.dto.UsersResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@Repository
public class UsersJdbcRepository implements UsersRepository {

    private final NamedParameterJdbcTemplate template;

    public UsersJdbcRepository(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public UsersResponseDto getCountableInfo(Long memberId) {
        String sql = "SELECT M.username," +
                " COUNT( F1.followee_id) AS followees," +
                " COUNT( F2.follower_id) AS followers," +
                " COUNT( L.member_id) AS totalLikes" +
                " FROM member M" +
                " LEFT JOIN follow F1 ON M.member_id = F1.follower_id" +
                " LEFT JOIN follow F2 ON M.member_id = F2.followee_id" +
                " LEFT JOIN likes L ON M.member_id = L.member_id" +
                " WHERE M.member_id = :memberId" +
                " GROUP BY M.member_id;";
        Map<String, Object> param = Map.of("memberId", memberId);
        return template.queryForObject(sql, param, myPageResponseDtoRowMapper());
    }

    @Override
    public List<PostResponseDto> getSummaryPosts(Long memberId) {
        String sql = "SELECT post_id, title, views, likes, created_at, updated_at " +
                "FROM post WHERE member_id = :memberId";
        Map<String, Object> param = Map.of("memberId", memberId);
        return template.query(sql, param, postResponseDtoRowMapper());
    }

    @Override
    public List<HashtagDto> getSummaryHashtags(Long memberId) {
        String sql = "SELECT DISTINCT H.hashtag_id, H.hashtag" +
                " FROM hashtag H" +
                " JOIN post_hashtag PH ON H.hashtag_id = PH.hashtag_id" +
                " JOIN post P ON PH.post_id = P.post_id" +
                " WHERE P.member_id = :memberId";
        Map<String, Object> param = Map.of("memberId", memberId);
        return template.query(sql, param, hashtagDtoRowMapper());
    }


    @Override
    public List<ColorDto> getSummaryColors(Long memberId) {
        String sql = "SELECT DISTINCT C.color_id, C.hex_color" +
                " FROM color C" +
                " JOIN post_color PC ON C.color_id = PC.color_id" +
                " JOIN post P ON PC.post_id = P.post_id" +
                " WHERE P.member_id = :memberId";
        Map<String, Object> param = Map.of("memberId", memberId);
        return template.query(sql, param, colorDtoRowMapper());
    }

    @Override
    public Page<PostResponseDto> getPosts(Long memberId, Pageable pageable) {
        String countSql = "SELECT COUNT(*) FROM post WHERE member_id = :memberId";
        Map<String, Object> param = Map.of("memberId", memberId);
        Integer total = template.queryForObject(countSql, param, Integer.class);
        assert total != null;

        String sql = "SELECT post_id, title, views, likes, created_at, updated_at" +
                " FROM post WHERE member_id = :memberId" +
                " ORDER BY :sort LIMIT :limit OFFSET :offset";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("memberId", memberId)
                .addValue("sort", pageable.getSort())
                .addValue("limit", pageable.getPageSize())
                .addValue("offset", pageable.getOffset());
        List<PostResponseDto> posts = template.query(sql, params, postResponseDtoRowMapper());
        return new PageImpl<>(posts, pageable, total);
    }

    @Override
    public Page<HashtagDto> getHashtags(Long memberId, Pageable pageable) {
        String countSql = "SELECT COUNT(DISTINCT H.hashtag_id) FROM Hashtag H" +
                " JOIN post_hashtag PH ON H.hashtag_id = PH.hashtag_id" +
                " JOIN post P ON PH.post_id = P.post_id" +
                " WHERE P.member_id = :memberId;";
        Map<String, Object> param = Map.of("memberId", memberId);
        Integer total = template.queryForObject(countSql, param, Integer.class);
        assert total != null;

        String sql = "SELECT DISTINCT H.hashtag_id, H.hashtag FROM hashtag H" +
                " JOIN post_hashtag PH ON H.hashtag_id = PH.hashtag_id" +
                " JOIN post P ON PH.post_id = P.post_id" +
                " WHERE P.member_id = :memberId ORDER BY hashtag ASC" +
                " LIMIT :limit OFFSET :offset";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("memberId", memberId)
                .addValue("limit", pageable.getPageSize())
                .addValue("offset", pageable.getOffset());
        List<HashtagDto> hashtags = template.query(sql, params, hashtagDtoRowMapper());
        return new PageImpl<>(hashtags, pageable, total);
    }

    //TODO:: 추후에 MemberDetails 만든 후 FollowDto -> MemberDetailsDto
    @Override
    public Page<FollowDto> getFollowers(Long memberId, Pageable pageable) {
        String countSql = "SELECT COUNT(*) FROM follow WHERE followee_id = :memberId";
        Map<String, Object> param = Map.of("memberId", memberId);
        Integer total = template.queryForObject(countSql, param, Integer.class);
        assert total != null;

        String sql = "SELECT * FROM follow WHERE followee_id = :memberId" +
                " ORDER BY follower_id ASC LIMIT :limit OFFSET :offset";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("memberId", memberId)
                .addValue("limit", pageable.getPageSize())
                .addValue("offset", pageable.getOffset());
        List<FollowDto> followers = template.query(sql, params, followDtoRowMapper());
        return new PageImpl<>(followers, pageable, total);
    }

    //TODO:: 추후에 MemberDetails 만든 후 FollowDto -> MemberDetailsDto
    @Override
    public Page<FollowDto> getFollowees(Long memberId, Pageable pageable) {
        String countSql = "SELECT COUNT(*) FROM follow WHERE follower_id = :memberId";
        Map<String, Object> param = Map.of("memberId", memberId);
        Integer total = template.queryForObject(countSql, param, Integer.class);
        assert total != null;

        String sql = "SELECT * FROM follow WHERE follower_id = :memberId" +
                " ORDER BY followee_id ASC LIMIT :limit OFFSET :offset";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("memberId", memberId)
                .addValue("limit", pageable.getPageSize())
                .addValue("offset", pageable.getOffset());
        List<FollowDto> followees = template.query(sql, params, followDtoRowMapper());
        return new PageImpl<>(followees, pageable, total);
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

    private RowMapper<FollowDto> followDtoRowMapper() {
        return BeanPropertyRowMapper.newInstance(FollowDto.class);
    }
}
