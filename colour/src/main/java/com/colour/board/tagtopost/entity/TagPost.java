package com.colour.board.tagtopost.entity;

import lombok.Data;

@Data
public class TagPost {

    private Long tagPostId;
    private Long tagId;
    private Long postId;
    private Long memberId;

    public TagPost(Long tagId, Long postId, Long memberId) {
        this.tagId = tagId;
        this.postId = postId;
        this.memberId = memberId;
    }

    public TagPost() {
    }
}
