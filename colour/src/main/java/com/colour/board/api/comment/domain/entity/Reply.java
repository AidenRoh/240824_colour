package com.colour.board.api.comment.domain.entity;

import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

@Data
public class Reply implements Response {

    private Long replyId;
    private Long commentId;
    private Long memberId;
    private String content;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public Reply(Long commentId, Long memberId, String content) {
        this.commentId = commentId;
        this.memberId = memberId;
        this.content = content;
        this.createdAt = new Timestamp(new Date().getTime());
    }

    public Reply() {
    }
}
