package com.colour.board.api.search.repository;

import com.colour.board.api.colorpalette.domain.dto.ColorCond;
import org.springframework.jdbc.core.ResultSetExtractor;
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

    public Map<Long, Integer> colorSearch(List<ColorCond> conds) {
        if (conds == null || conds.isEmpty()) {
            return Collections.emptyMap();
        }

        StringBuilder sql = new StringBuilder("WITH ColorFilter AS (\nSELECT pc.post_id,\n");
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
                        WHERE""")
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

        return template.query(sql.toString(), params, resultSetExtractor());
    }

    private ResultSetExtractor<Map<Long, Integer>> resultSetExtractor() {
        return rs -> {
            Map<Long, Integer> resultMap = new HashMap<>();
            while (rs.next()) {
                long postId = rs.getLong("post_id");
                int matchScore = rs.getInt("match_score");
                resultMap.put(postId, matchScore);
            }
            return resultMap;
        };
    }

}
