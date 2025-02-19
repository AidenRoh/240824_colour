package com.colour.board.post.entity;

import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

@Data
public class Post {

    private static final long DEFAULT_LIKE = 0;

    private Long postId;
    private String writer;
    private String title;
    private String content;
    private String colorPalette;
    private Long memberLikes;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public Post(String writer, String title, String content, String colorPalette) {
        this.writer = writer;
        this.title = title;
        this.content = content;
        this.colorPalette = colorPalette;
        this.memberLikes = DEFAULT_LIKE;
        this.createdAt = new Timestamp(new Date().getTime());
    }

    public Post() {
    }
}
