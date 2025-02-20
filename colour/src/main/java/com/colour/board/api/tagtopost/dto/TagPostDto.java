package com.colour.board.api.tagtopost.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TagPostDto {
    private Long tagId;
    private Long postId;
    private Long memberId;
}
