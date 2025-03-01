package com.colour.board.api.posthashtag.domain.entity;

import lombok.Data;

@Data
public class PostHashtag {

    private Long postHashtagId;
    private Long hashtagId;
    private Long postId;

    public PostHashtag(Long hashtagId, Long postId) {
        this.hashtagId = hashtagId;
        this.postId = postId;
    }

    public PostHashtag() {
    }
}
