package com.colour.member.repository;

import com.colour.member.dto.MemberUpdateDto;
import com.colour.member.entity.Member;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.Map;
import java.util.Optional;

public class MemberRepositoryJDBC implements MemberRepository {

    private final NamedParameterJdbcTemplate template;

    public MemberRepositoryJDBC(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public Member create(Member member) {
        String sql = "INSERT INTO member (username, email, password) " +
                "VALUES(:username, :email, :password)";

        SqlParameterSource param = new BeanPropertySqlParameterSource(member);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(sql, param, keyHolder);

//        template.update(connection -> {
//            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
//            ps.setString(1, member.getUsername());
//            ps.setString(2, member.getEmail());
//            ps.setString(3, member.getPassword());
//            return ps;
//        }, keyHolder);

        long key = keyHolder.getKey().longValue();
        member.setId(key);
        return member;
    }

    @Override
    public void update(Long memberId, MemberUpdateDto updateDto) {
        String sql = "UPDATE member SET username=?, password=? WHERE id=?";
        if (updateDto.get)
    }

    @Override
    public Optional<Member> findById(Long memberId) {
        String sql = "SELECT id, username, email, password FROM member WHERE id = :id";
        try {
            Map<String, Object> param = Map.of("id", memberId);
            Member member = template.queryForObject(sql, param, memberRowMapper());
            assert member != null;
            return Optional.of(member);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private RowMapper<Member> memberRowMapper() {
        return BeanPropertyRowMapper.newInstance(Member.class);
//        return ((rs, rowNum) -> {
//            Member member = new Member();
//            member.setId(rs.getLong("id"));
//            member.setUsername(rs.getString("username"));
//            member.setEmail(rs.getString("email"));
//            return member;
//        });
    }

    @Override
    public Member findAll() {
        return null;
    }

    @Override
    public void delete(Long memberId) {

    }
}
