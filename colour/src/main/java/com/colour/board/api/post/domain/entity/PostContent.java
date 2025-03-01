package com.colour.board.api.post.domain.entity;

import lombok.Data;

@Data
public class PostContent {

    Long contentId;
    Long postId;
    String content;

    public PostContent(Long postId, String content) {
        this.postId = postId;
        this.content = content;
    }

    public PostContent() {
    }
}
