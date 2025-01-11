package com.colour.comment.entity;

import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

@Data
public class Reply implements Response{

    private Long replyId;
    private Long commentId;
    private Long memberId;
    private String writer;
    private String comment;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public Reply(Long commentId, Long memberId, String writer, String comment) {
        this.commentId = commentId;
        this.memberId = memberId;
        this.writer = writer;
        this.comment = comment;
        this.createdAt = new Timestamp(new Date().getTime());
    }

    public Reply() {
    }
}
