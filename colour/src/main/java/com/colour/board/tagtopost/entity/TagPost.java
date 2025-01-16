package com.colour.board.tagtopost.entity;

import lombok.Data;

@Data
public class TagPost {

    private Long tagPostId;
    private Long hashtagId;
    private Long postId;
    private Long memberId;

    public TagPost(Long hashtagId, Long postId, Long memberId) {
        this.hashtagId = hashtagId;
        this.postId = postId;
        this.memberId = memberId;
    }

    public TagPost() {
    }
}
