package com.colour.member.repository;

import com.colour.member.dto.MemberUpdateDto;
import com.colour.member.entity.Member;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Optional;

@Repository
public class MemberRepositoryJDBC implements MemberRepository {

    private final NamedParameterJdbcTemplate template;
    private final SimpleJdbcInsert insert;

    public MemberRepositoryJDBC(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
        this.insert = new SimpleJdbcInsert(dataSource)
                .withTableName("member")
                .usingGeneratedKeyColumns("member_id");
    }

    @Override
    public Member create(Member member) {
//        String sql = "INSERT INTO member (username, email, password) " +
//                "VALUES(:username, :email, :password)";
//
//        SqlParameterSource param = new BeanPropertySqlParameterSource(member);
//        KeyHolder keyHolder = new GeneratedKeyHolder();
//        template.update(sql, param, keyHolder);

//        template.update(connection -> {
//            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
//            ps.setString(1, member.getUsername());
//            ps.setString(2, member.getEmail());
//            ps.setString(3, member.getPassword());
//            return ps;
//        }, keyHolder);
//        long key = keyHolder.getKey().longValue();
        SqlParameterSource param = new BeanPropertySqlParameterSource(member);
        Number key = insert.executeAndReturnKey(param);
        member.setMemberId(key.longValue());
        return member;
    }

    @Override
    public void update(Long memberId, MemberUpdateDto updateDto) {
        String sql = "UPDATE member SET ";
        boolean andFlag = updateDto.getUsername() != null && updateDto.getPassword() != null;
        if (updateDto.getUsername() != null) sql += "username=:username";
        if (updateDto.getPassword() != null) {
            if (andFlag) {
                sql += ", ";
            }
            sql += "password=:password";
        }
        sql += " WHERE member_id=:id";

        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("id", memberId)
                .addValue("username", updateDto.getUsername())
                .addValue("password", updateDto.getPassword());

        template.update(sql, param);
    }

    @Override
    public Optional<Member> findById(Long memberId) {
        String sql = "SELECT member_id, username, email, password FROM member WHERE member_id = :id";
        try {
            Map<String, Object> param = Map.of("id", memberId);
            Member member = template.queryForObject(sql, param, memberRowMapper());
            assert member != null;
            return Optional.of(member);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Member findAll() {
        return null;
    }

    @Override
    public void delete(Long memberId) {
        String sql = "DELETE FROM member WHERE member_id = :id";
        SqlParameterSource param = new MapSqlParameterSource().addValue("id", memberId);
        template.update(sql, param);
    }

    @Override
    public boolean existsByEmail(String email) {
        String sql = "SELECT count(*) FROM member WHERE email = :email";
        Map<String, Object> param = Map.of("email", email);
        Long count = template.queryForObject(sql, param, Long.class);
        return count > 0;
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
}
