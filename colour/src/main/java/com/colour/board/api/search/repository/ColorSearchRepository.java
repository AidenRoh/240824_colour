package com.colour.board.api.search.repository;

import com.colour.board.api.colorpalette.domain.dto.ColorCond;
import com.colour.board.api.colorpalette.domain.entity.HexColor;
import com.colour.board.api.search.domain.dto.ColorMatchScore;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
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

    public List<ColorMatchScore> monoColorSearchByHex(List<HexColor> colors) {
        StringBuilder sql = new StringBuilder("WITH ColorFilter AS ( SELECT pc.post_id, ");
        Map<String, Object> params = new HashMap<>();
        StringBuilder caseConditions = new StringBuilder();
        StringBuilder whereConditions = new StringBuilder();
        StringBuilder matchScore = new StringBuilder();
        StringBuilder orderPriority = new StringBuilder();
        for (int i = 0; i < colors.size(); i++) {
            String subCondition = "c.hue = :hue" + i
                    + " AND c.saturation = :saturation" + i
                    + " AND c.lightness = :lightness" + i;
            String conditionName = "condition_" + i;

            if (i > 0) {
                caseConditions.append(", ");
                whereConditions.append(" OR ");
                matchScore.append(" + ");
                orderPriority.append(" + ");
            }
            caseConditions.append("SUM(CASE WHEN ").append(subCondition).append(" THEN 1 ELSE 0 END) AS ").append(conditionName);
            whereConditions.append("(").append(subCondition).append(")");
            matchScore.append("(CASE WHEN ").append(conditionName).append(" > 0 THEN 1 ELSE 0 END)");
            orderPriority.append(conditionName);

            HexColor each = colors.get(i);
            params.put("hue" + i, each.getHue());
            params.put("saturation" + i, each.getSaturation());
            params.put("lightness" + i, each.getLightness());
        }
        sql.append(caseConditions)
                .append("""
                        \nFROM post_color pc
                        JOIN color c ON pc.color_id = c.color_id
                        WHERE
                        """)
                .append("(").append(whereConditions).append(")")
                .append("""
                        \nGROUP BY pc.post_id
                        )
                        SELECT post_id,
                        """)
                .append("(").append(matchScore).append(") AS match_score")
                .append("""
                        \nFROM ColorFilter
                        ORDER BY match_score DESC,
                        """)
                .append(orderPriority).append(" DESC");

        return template.query(sql.toString(), params, colorMatchScoreRowMapper());
    }

    public List<ColorMatchScore> monoColorSearchByCond(List<ColorCond> conds) {
        if (conds == null || conds.isEmpty()) {
            return Collections.emptyList();
        }

        StringBuilder sql = new StringBuilder("WITH ColorFilter AS ( SELECT pc.post_id, ");
        Map<String, Object> params = new HashMap<>();
        StringBuilder caseConditions = new StringBuilder();
        StringBuilder whereConditions = new StringBuilder();
        StringBuilder matchScore = new StringBuilder();
        StringBuilder orderPriority = new StringBuilder();
        for (int i = 0; i < conds.size(); i++) {
            ColorCond each = conds.get(i);
            StringBuilder subConditions = new StringBuilder();
            String conditionName = "condition_" + i;

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
                if (i > 0) {
                    caseConditions.append(", ");
                    whereConditions.append(" OR ");
                    matchScore.append(" + ");
                    orderPriority.append(" + ");
                }
                caseConditions.append("SUM(CASE WHEN ").append(subConditions).append(" THEN 1 ELSE 0 END) AS ").append(conditionName);
                whereConditions.append("(").append(subConditions).append(")");
                matchScore.append("(CASE WHEN ").append(conditionName).append(" > 0 THEN 1 ELSE 0 END)");
                orderPriority.append(conditionName);
            }

            params.put("hue" + i, each.getHue());
            params.put("saturation" + i, each.getSaturation());
            params.put("lightness" + i, each.getLightness());
        }
        sql.append(caseConditions)
                .append("""
                        \nFROM post_color pc
                        JOIN color c ON pc.color_id = c.color_id
                        WHERE
                        """)
                .append("(").append(whereConditions).append(")")
                .append("""
                        \nGROUP BY pc.post_id
                        )
                        SELECT post_id,
                        """)
                .append("(").append(matchScore).append(") AS match_score")
                .append("""
                        \nFROM ColorFilter
                        ORDER BY match_score DESC,
                        """)
                .append(orderPriority).append(" DESC");

        return template.query(sql.toString(), params, colorMatchScoreRowMapper());
    }

    private RowMapper<ColorMatchScore> colorMatchScoreRowMapper() {
        return BeanPropertyRowMapper.newInstance(ColorMatchScore.class);
    }

}
