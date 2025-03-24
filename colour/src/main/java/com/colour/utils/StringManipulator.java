package com.colour.utils;

import org.springframework.data.domain.Pageable;

public class StringManipulator {

    public static String buildOrderByClause(Pageable pageable) {
        StringBuilder builder = new StringBuilder();
        pageable.getSort().forEach(sort -> {
            builder.append(sort.getProperty()).append(" ").append(sort.getDirection());
        });
        return builder.toString();
    }
}
