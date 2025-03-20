package com.colour.board.api.search.service;

import com.colour.board.api.search.domain.enums.WeightType;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class WeightCalculationService {

    public Map<Long, Double> getUnion(Map<Long, Integer> color, Map<Long, Integer> hashtag,
                                      WeightType defaultType, WeightType intersectionType) {
        double normal = defaultType.getCoefficient();
        double intersection = intersectionType.getCoefficient();
        Map<Long, Double> combinedMap = new HashMap<>();
        color.forEach((k, v) -> combinedMap.put(k, (v * normal)));

        hashtag.forEach((postId, score) ->
                combinedMap.compute(postId, (k, v) ->
                        (v == null) ? (score * normal) : (v + score) * intersection)
        );
        return combinedMap;
    }
}
