package com.colour.board.api.search.util;

import java.util.*;

public class Intersection {

    public static List<Long> findIntersection(List<List<Long>> lists) {
        if (lists == null || lists.isEmpty()) {
            return Collections.emptyList();
        }

        Map<Long, Integer> countMap = new HashMap<>();
        int listCount = lists.size();

        for (List<Long> eachList : lists) {
            Set<Long> uniqueSet = new HashSet<>(eachList);
            for (Long each : uniqueSet) {
                countMap.put(each, countMap.getOrDefault(each, 0) + 1);
            }
        }

        return countMap.entrySet().stream()
                .filter(entry -> entry.getValue() == listCount)
                .map(Map.Entry::getKey)
                .toList();
    }
}
