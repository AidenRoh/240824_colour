package com.colour.board.api.hashtag.repository;

import com.colour.board.api.hashtag.domain.entity.Hashtag;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Optional;

public class HashtagJdbcRepository implements HashtagRepository {

    private final NamedParameterJdbcTemplate template;
    private final SimpleJdbcInsert insert;

    public HashtagJdbcRepository(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
        this.insert = new SimpleJdbcInsert(dataSource)
                .withTableName("hashtag")
                .usingGeneratedKeyColumns("hashtag_id");
    }

    @Override
    public Hashtag save(Hashtag hashtag) {
        SqlParameterSource source = new BeanPropertySqlParameterSource(hashtag);
        Number key = insert.executeAndReturnKey(source);
        hashtag.setHashtagId(key.longValue());
        return hashtag;
    }

    @Override
    public void tagUp(Long hashtagId) {
        String sql = "UPDATE hashtag SET tag_frequency = hashtag.tag_frequency + 1 WHERE hashtag_id=:hashtagId";
        Map<String, Object> param = Map.of("hashtagId", hashtagId);
        template.update(sql, param);
    }

    @Override
    public void tagDown(Long hashtagId) {
        String sql = "UPDATE hashtag SET tag_frequency = hashtag.tag_frequency - 1 WHERE hashtag_id=:hashtagId";
        Map<String, Object> param = Map.of("hashtagId", hashtagId);
        template.update(sql, param);
    }

    @Override
    public Optional<Hashtag> findById(Long hashtagId) {
        String sql = "SELECT * FROM hashtag WHERE hashtag_id=:hashtagId";
        try {
            Map<String, Object> param = Map.of("hashtagId", hashtagId);
            Hashtag tag = template.queryForObject(sql, param, hashtagRowMapper());
            assert tag != null;
            return Optional.of(tag);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    // tag field has only one element: tag , so that it doesn't need condition dto
    @Override
    public Optional<Hashtag> findByTag(String tagName) {
        String sql = "SELECT * FROM hashtag WHERE hashtag=:tagName";
        try {
            Map<String, Object> param = Map.of("tagName", tagName);
            Hashtag tag = template.queryForObject(sql, param, hashtagRowMapper());
            assert tag != null;
            return Optional.of(tag);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void delete(Long hashtagId) {
        String sql = "DELETE FROM hashtag WHERE hashtag_id=:tagId";
        Map<String, Object> param = Map.of("tagId", hashtagId);
        template.update(sql, param);
    }

    private RowMapper<Hashtag> hashtagRowMapper() {
        return new BeanPropertyRowMapper<>(Hashtag.class);
    }
}
