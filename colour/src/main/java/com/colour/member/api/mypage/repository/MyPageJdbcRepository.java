package com.colour.member.api.mypage.repository;

import com.colour.board.api.colorpalette.domain.dto.ColorDto;
import com.colour.board.api.hashtag.domain.dto.HashtagDto;
import com.colour.board.api.post.domain.dto.PostResponseDto;
import com.colour.member.api.mypage.dto.MyPageResponseDto;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@Repository
public class MyPageJdbcRepository implements MyPageRepository {

    private final NamedParameterJdbcTemplate template;

    public MyPageJdbcRepository(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public MyPageResponseDto getCountableInfo(Long memberId) {
        String sql = "SELECT M.username, " +
                "COUNT( F1.followee_id) AS followees, " +
                "COUNT( F2.follower_id) AS followers, " +
                "COUNT( L.member_id) AS totalLikes " +
                "FROM member M " +
                "LEFT JOIN follow F1 ON M.member_id = F1.follower_id " +
                "LEFT JOIN follow F2 ON M.member_id = F2.followee_id " +
                "LEFT JOIN likes L ON M.member_id = L.member_id " +
                "WHERE M.member_id = :memberId " +
                "GROUP BY M.member_id;";
        Map<String, Object> param = Map.of("memberId", memberId);
        return template.queryForObject(sql, param, myPageResponseDtoRowMapper());
    }

    @Override
    public List<PostResponseDto> getPosts(Long memberId) {
        String sql = "SELECT post_id, title, views, likes, created_at, updated_at " +
                "FROM post WHERE member_id = :memberId";
        Map<String, Object> param = Map.of("memberId", memberId);
        return template.query(sql, param, postResponseDtoRowMapper());
    }

    @Override
    public List<HashtagDto> getHashtags(Long memberId) {
        String sql = "SELECT DISTINCT H.hashtag " +
                "FROM hashtag H " +
                "JOIN post_hashtag PH ON H.hashtag_id = PH.hashtag_id " +
                "JOIN post P ON PH.post_id = P.post_id " +
                "WHERE P.member_id = :memberId";
        Map<String, Object> param = Map.of("memberId", memberId);
        return template.query(sql, param, hashtagDtoRowMapper());
    }


    @Override
    public List<ColorDto> getColors(Long memberId) {
        String sql = "SELECT DISTINCT C.hex_color " +
                "FROM color C " +
                "JOIN post_color PC ON C.color_id = PC.color_id " +
                "JOIN post P ON PC.post_id = P.post_id " +
                "WHERE P.member_id = :memberId";
        Map<String, Object> param = Map.of("memberId", memberId);
        return template.query(sql, param, colorDtoRowMapper());
    }

    private RowMapper<MyPageResponseDto> myPageResponseDtoRowMapper() {
        return BeanPropertyRowMapper.newInstance(MyPageResponseDto.class);
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
