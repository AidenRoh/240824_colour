package com.colour.board.api.search.repository;

import com.colour.board.api.colorpalette.domain.dto.ColorCond;
import com.colour.board.api.colorpalette.domain.entity.HexColor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ColorSearchRepository {

    private final NamedParameterJdbcTemplate template;

    public ColorSearchRepository(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
    }

    public List<Long> searchByColorOR(List<HexColor> colors) {
        StringBuilder sql = new StringBuilder("""
                SELECT pc.post_id
                FROM post_color pc
                JOIN color c ON pc.color_id = c.color_id
                WHERE
                """);
        Map<String, Object> params = new HashMap<>();
        StringBuilder conditions = new StringBuilder();
        for (int i = 0; i < colors.size(); i++) {
            if (i > 0) conditions.append(" OR ");
            conditions.append("(c.hue = :hue").append(i)
                    .append(" AND c.saturation = :saturation").append(i)
                    .append(" AND c.lightness = :lightness").append(i).append(")");

            HexColor each = colors.get(i);
            params.put("hue" + i, each.getHue());
            params.put("saturation" + i, each.getSaturation());
            params.put("lightness" + i, each.getLightness());
        }
        sql.append("(").append(conditions).append(")").append(" ORDER BY NULL");

        return template.queryForList(sql.toString(), params, Long.class);
    }

    public List<Long> searchByColorAND(List<HexColor> colors) {
        StringBuilder sql = new StringBuilder("""
                SELECT pc.post_id
                FROM post_color pc
                JOIN color c ON pc.color_id = c.color_id
                WHERE
                """);
        Map<String, Object> params = new HashMap<>();
        StringBuilder whenConditions = new StringBuilder();
        StringBuilder caseConditions = new StringBuilder();
        for (int i = 0; i < colors.size(); i++) {
            String subCondition = "c.hue = :hue" + i
                    + " AND c.saturation = :saturation" + i
                    + " AND c.lightness = :lightness" + i;
            if (i > 0) whenConditions.append(" OR ");
            whenConditions.append("(").append(subCondition).append(")");
            caseConditions.append("WHEN ").append(subCondition).append(" THEN 1\n");

            HexColor each = colors.get(i);
            params.put("hue" + i, each.getHue());
            params.put("saturation" + i, each.getSaturation());
            params.put("lightness" + i, each.getLightness());
        }
        sql.append("(").append(whenConditions).append(")").append("""
                \nGROUP BY pc.post_id
                HAVING COUNT(CASE
                """).append(caseConditions).append("""
                END) = :colorCount
                ORDER BY NULL
                """);
        params.put("colorCount", colors.size());

        return template.queryForList(sql.toString(), params, Long.class);
    }

    public List<Long> searchByColorCondOR(List<ColorCond> conds) {
        if (conds == null || conds.isEmpty()) {
            return Collections.emptyList();
        }
        StringBuilder sql = new StringBuilder("""
                SELECT pc.post_id
                FROM post_color pc
                JOIN color c ON pc.color_id = c.color_id
                WHERE
                """);
        Map<String, Object> params = new HashMap<>();
        StringBuilder conditions = new StringBuilder();
        for (int i = 0; i < conds.size(); i++) {
            ColorCond each = conds.get(i);
            StringBuilder subConditions = new StringBuilder();

            if (each.getHue() != null) {
                subConditions.append("c.hue = :hue").append(i);
                params.put("hue" + i, each.getHue());
            }
            if (each.getSaturation() != null) {
                if (!subConditions.isEmpty()) subConditions.append(" AND ");
                subConditions.append("c.saturation = :saturation").append(i);
                params.put("saturation" + i, each.getSaturation());
            }
            if (each.getLightness() != null) {
                if (!subConditions.isEmpty()) subConditions.append(" AND ");
                subConditions.append("c.lightness = :lightness").append(i);
                params.put("lightness" + i, each.getLightness());
            }
            if (!subConditions.isEmpty()) {
                if (i > 0) conditions.append(" OR ");
                conditions.append("(").append(subConditions).append(")");
            }
        }
        sql.append("(").append(conditions).append(")").append(" ORDER BY NULL");

        return template.queryForList(sql.toString(), params, Long.class);
    }

    public List<Long> searchByColorCondAND(List<ColorCond> conds) {
        StringBuilder sql = new StringBuilder("""
                SELECT pc.post_id
                FROM post_color pc
                JOIN color c ON pc.color_id = c.color_id
                WHERE
                """);
        Map<String, Object> params = new HashMap<>();
        StringBuilder whenConditions = new StringBuilder();
        StringBuilder caseConditions = new StringBuilder();
        for (int i = 0; i < conds.size(); i++) {
            ColorCond each = conds.get(i);
            StringBuilder subConditions = new StringBuilder();

            if (each.getHue() != null) {
                subConditions.append("c.hue = :hue").append(i);
                params.put("hue" + i, each.getHue());
            }
            if (each.getSaturation() != null) {
                if (!subConditions.isEmpty()) subConditions.append(" AND ");
                subConditions.append("c.saturation = :saturation").append(i);
                params.put("saturation" + i, each.getSaturation());
            }
            if (each.getLightness() != null) {
                if (!subConditions.isEmpty()) subConditions.append(" AND ");
                subConditions.append("c.lightness = :lightness").append(i);
                params.put("lightness" + i, each.getLightness());
            }
            if (!subConditions.isEmpty()) {
                if (i > 0) whenConditions.append(" OR ");
                whenConditions.append("(").append(subConditions).append(")");
                caseConditions.append("WHEN ").append(subConditions).append(" THEN 1\n");
            }
        }
        sql.append("(").append(whenConditions).append(")").append("""
                \nGROUP BY pc.post_id
                HAVING COUNT(CASE
                """).append(caseConditions).append("""
                END) = :colorCount
                ORDER BY NULL
                """);
        params.put("colorCount", conds.size());

        return template.queryForList(sql.toString(), params, Long.class);
    }

}
