package com.colour.board.api.post.domain.enums;

import lombok.Getter;

@Getter
public enum PostStatus {
    TEMPORARY("TEMPORARY"),
    POSTED("POSTED"),
    PRIVACY("PRIVACY"),
    DELETED("DELETED");

    private final String status;

    PostStatus(String status) {
        this.status = status;
    }
}
