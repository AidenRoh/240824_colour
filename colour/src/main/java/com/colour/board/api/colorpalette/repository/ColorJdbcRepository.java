package com.colour.board.api.colorpalette.repository;

import com.colour.board.api.colorpalette.domain.entity.HexColor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Optional;

@Repository
public class ColorJdbcRepository implements ColorRepository {

    private final NamedParameterJdbcTemplate template;
    private final SimpleJdbcInsert insert;

    public ColorJdbcRepository(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
        this.insert = new SimpleJdbcInsert(dataSource)
                .withTableName("color")
                .usingGeneratedKeyColumns("color_id");
    }

    @Override
    public HexColor save(HexColor color) {
        SqlParameterSource params = new BeanPropertySqlParameterSource(color);
        Number colorId = insert.executeAndReturnKey(params);
        color.setColorId(colorId.longValue());
        return color;
    }

    @Override
    public Optional<HexColor> findByHex(String hexColor) {
        String sql = "SELECT * FROM color WHERE hex_color = :hexColor";
        Map<String, Object> param = Map.of("hexColor", hexColor);
        try {
            HexColor findColor = template.queryForObject(sql, param, hexColorRowMapper());
            assert findColor != null;
            return Optional.of(findColor);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void delete(Long colorId) {
        String sql = "DELETE FROM color WHERE color_id = :colorId";
        Map<String, Object> param = Map.of("colorId", colorId);
        template.update(sql, param);
    }

    private RowMapper<HexColor> hexColorRowMapper() {
        return BeanPropertyRowMapper.newInstance(HexColor.class);
    }
}
