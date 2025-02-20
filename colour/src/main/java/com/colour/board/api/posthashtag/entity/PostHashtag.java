package com.colour.board.api.posthashtag.entity;

import lombok.Data;

@Data
public class PostHashtag {

    private Long postHashtagId;
    private Long hashtagId;
    private Long postId;
    private Long memberId;

    public PostHashtag(Long hashtagId, Long postId, Long memberId) {
        this.hashtagId = hashtagId;
        this.postId = postId;
        this.memberId = memberId;
    }

    public PostHashtag() {
    }
}
