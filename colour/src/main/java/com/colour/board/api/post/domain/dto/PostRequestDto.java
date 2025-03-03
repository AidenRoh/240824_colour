package com.colour.board.api.post.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostRequestDto {
    private String title;
    private String content;
    private String status;
    private boolean newPost;
}
