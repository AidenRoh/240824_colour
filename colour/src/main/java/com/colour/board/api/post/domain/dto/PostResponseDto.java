package com.colour.board.api.post.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class PostResponseDto {
    Long postId;
    String title;
    Long views;
    Long likes;
    Timestamp createdAt;
    Timestamp updatedAt;
}
