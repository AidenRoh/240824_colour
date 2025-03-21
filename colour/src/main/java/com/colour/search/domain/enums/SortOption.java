package com.colour.search.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Sort;

@AllArgsConstructor
@Getter
public enum SortOption {
    LATEST("created_at", Sort.Direction.DESC),
    EARLIEST("created_at", Sort.Direction.ASC),
    VIEWS("views", Sort.Direction.DESC),
    POPULAR("likes", Sort.Direction.DESC),
    RELEVANCE("_score", Sort.Direction.DESC);


    private final String field;
    private final Sort.Direction direction;

    public static Sort getSort(String value) {
        for (SortOption option : values()) {
            if (option.name().toLowerCase().equals(value)) return Sort.by(option.direction, option.field);
        }
        return Sort.unsorted();
    }
}
