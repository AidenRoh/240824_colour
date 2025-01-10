package com.colour.board.entity;

import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

@Data
public class Board {

    private static final long DEFAULT_LIKE = 0;

    private Long boardId;
    private String writer;
    private String title;
    private String content;
    private Long userLike;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public Board(String writer, String title, String content) {
        this.writer = writer;
        this.title = title;
        this.content = content;
        this.userLike = DEFAULT_LIKE;
        this.createdAt = new Timestamp(new Date().getTime());
    }



    public Board() {
    }
}
