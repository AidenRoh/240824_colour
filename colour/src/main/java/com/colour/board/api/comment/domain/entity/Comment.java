package com.colour.board.api.comment.domain.entity;

import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

@Data
public class Comment implements Response {

    private Long commentId;
    private Long boardId;
    private Long memberId;
    private String content;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public Comment(Long boardId, Long memberId, String content) {
        this.boardId = boardId;
        this.memberId = memberId;
        this.content = content;
        this.createdAt = new Timestamp(new Date().getTime());
    }

    public Comment() {
    }
}
