package com.colour.board.api.search.repository;

import com.colour.board.api.hashtag.domain.dto.HashtagDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class HashtagSearchRepository {

    private final JdbcTemplate template;

    public HashtagSearchRepository(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }

    public Map<Long, Integer> hashtagsSearch(List<HashtagDto> hashtags) {
        if (hashtags == null || hashtags.isEmpty()) {
            return Collections.emptyMap();
        }

        StringBuilder conditions = new StringBuilder();
        for (int i = 0; i < hashtags.size(); i++) {
            HashtagDto hashtag = hashtags.get(i);
            if (i > 0) {
                conditions.append(", ");
            }
            conditions.append("'").append(hashtag.getHashtag()).append("'");
        }
        StringBuilder sql = new StringBuilder("""
                SELECT ph.post_id,
                    SUM(CASE WHEN h.hashtag IN\s""")
                .append("(").append(conditions).append(")")
                .append("""
                        \sTHEN 1 ELSE 0 END) AS match_score
                        FROM post_hashtag ph
                        JOIN hashtag h ON ph.hashtag_id = h.hashtag_id
                        WHERE h.hashtag IN """)
                .append("(").append(conditions).append(")")
                .append("""
                        \nGROUP BY ph.post_id
                        ORDER BY match_score DESC
                        """);

        return template.query(sql.toString(), resultSetExtractor());
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
