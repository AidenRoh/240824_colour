package com.colour.board.api.postcolor.domain.entity;

import lombok.Data;

@Data
public class PostColor {

    private Long postId;
    private Long colorId;

    public PostColor(Long postId, Long colorId) {
        this.postId = postId;
        this.colorId = colorId;
    }

    public PostColor() {
    }
}
