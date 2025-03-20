package com.colour.board.api.search.domain.enums;

import lombok.Getter;

@Getter
public enum WeightType {
    ELASTIC_DEFAULT(0.5),
    ELASTIC_INTERSECTION(0.75),
    FILTER_ONLY_DEFAULT(1),
    FILTER_ONLY_INTERSECTION(1.5);

    private final double coefficient;

    WeightType(double coefficient) {
        this.coefficient = coefficient;
    }
}
