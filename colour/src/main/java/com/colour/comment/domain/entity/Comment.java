package com.colour.comment.domain.entity;

import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

@Data
public class Comment implements Response {

    private Long commentId;
    private Long boardId;
    private Long memberId;
    private String writer;
    private String comment;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public Comment(Long boardId, Long memberId, String writer, String comment) {
        this.boardId = boardId;
        this.memberId = memberId;
        this.writer = writer;
        this.comment = comment;
        this.createdAt = new Timestamp(new Date().getTime());
    }

    public Comment() {
    }
}
