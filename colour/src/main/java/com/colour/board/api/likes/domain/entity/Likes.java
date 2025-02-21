package com.colour.board.api.likes.domain.entity;

import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

@Data
public class Likes {

    private Long postId;
    private Long memberId;
    private Timestamp createdAt;

    public Likes(Long postId, Long memberId) {
        this.postId = postId;
        this.memberId = memberId;
        this.createdAt = new Timestamp(new Date().getTime());
    }

    public Likes() {
    }
}
