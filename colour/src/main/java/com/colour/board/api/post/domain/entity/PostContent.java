package com.colour.board.api.post.domain.entity;

import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

@Data
public class PostContent {

    Long contentId;
    Long postId;
    String content;
    Timestamp updatedAt;

    public PostContent(Long postId, String content) {
        this.postId = postId;
        this.content = content;
        this.updatedAt = new Timestamp(new Date().getTime());
    }

    public PostContent() {
    }
}
