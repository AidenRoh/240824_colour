package com.colour.board.api.post.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Date;

@Getter
@Setter
public class PostRequestDto {
    private String title;
    private String content;
    private String status;
    private Timestamp timestamp;
    private boolean newPost;

    public PostRequestDto() {
        this.timestamp = new Timestamp(new Date().getTime());
    }
}
