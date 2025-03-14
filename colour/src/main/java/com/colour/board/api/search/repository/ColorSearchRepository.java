package com.colour.board.api.search.repository;

import com.colour.board.api.colorpalette.domain.dto.ColorCond;
import com.colour.board.api.colorpalette.domain.entity.HexColor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.*;

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
        List<String> conditions = new ArrayList<>();
        for (int i = 0; i < colors.size(); i++) {
            HexColor each = colors.get(i);
            conditions.add("(c.hue = :hue" + i
                    + " AND c.saturation = :saturation" + i
                    + " AND c.lightness = :lightness" + i + ")");
            params.put("hue" + i, each.getHue());
            params.put("saturation" + i, each.getSaturation());
            params.put("lightness" + i, each.getLightness());
        }
        sql.append("(").append(String.join(" OR ", conditions)).append(")").append(" ORDER BY NULL");

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
        List<String> whenConditions = new ArrayList<>();
        List<String> caseConditions = new ArrayList<>();
        for (int i = 0; i < colors.size(); i++) {
            HexColor each = colors.get(i);
            String subCondition = "c.hue = :hue" + i
                    + " AND c.saturation = :saturation" + i
                    + " AND c.lightness = :lightness" + i;
            whenConditions.add("(" + subCondition + ")");
            caseConditions.add("WHEN " + subCondition + " THEN 1");
            params.put("hue" + i, each.getHue());
            params.put("saturation" + i, each.getSaturation());
            params.put("lightness" + i, each.getLightness());
        }
        sql.append("(").append(String.join(" OR ", whenConditions)).append(")").append("""
                \nGROUP BY pc.post_id
                HAVING COUNT(CASE
                """).append(String.join("\n", caseConditions)).append("""
                \nEND) = :colorCount
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
        List<String> conditions = new ArrayList<>();
        for (int i = 0; i < conds.size(); i++) {
            ColorCond each = conds.get(i);
            List<String> subConditions = new ArrayList<>();

            if (each.getHue() != null) {
                subConditions.add("c.hue = :hue" + i);
                params.put("hue" + i, each.getHue());
            }
            if (each.getSaturation() != null) {
                subConditions.add("c.saturation = :saturation" + i);
                params.put("saturation" + i, each.getSaturation());
            }
            if (each.getLightness() != null) {
                subConditions.add("c.lightness = :lightness" + i);
                params.put("lightness" + i, each.getLightness());
            }
            if (!subConditions.isEmpty()) {
                conditions.add("(" + String.join(" AND ", subConditions) + ")");
            }
        }
        sql.append("(").append(String.join(" OR ", conditions)).append(")").append(" ORDER BY NULL");

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
        List<String> whenConditions = new ArrayList<>();
        List<String> caseConditions = new ArrayList<>();
        for (int i = 0; i < conds.size(); i++) {
            ColorCond each = conds.get(i);
            List<String> subConditions = new ArrayList<>();

            if (each.getHue() != null) {
                subConditions.add("c.hue = :hue" + i);
                params.put("hue" + i, each.getHue());
            }
            if (each.getSaturation() != null) {
                subConditions.add("c.saturation = :saturation" + i);
                params.put("saturation" + i, each.getSaturation());
            }
            if (each.getLightness() != null) {
                subConditions.add("c.lightness = :lightness" + i);
                params.put("lightness" + i, each.getLightness());
            }
            if (!subConditions.isEmpty()) {
                String subCondition = String.join(" AND ", subConditions);
                whenConditions.add("(" + subCondition + ")");
                caseConditions.add("WHEN " + subCondition + " THEN 1");
            }
        }
        sql.append("(").append(String.join(" OR ", whenConditions)).append(")").append("""
                \nGROUP BY pc.post_id
                HAVING COUNT(CASE
                """).append(String.join("\n", caseConditions)).append("""
                \nEND) = :colorCount
                ORDER BY NULL
                """);
        params.put("colorCount", conds.size());

        return template.queryForList(sql.toString(), params, Long.class);
    }

}
