package com.colour.board.api.posthashtag.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostHashtagDto {
    private Long tagId;
    private Long postId;
    private Long memberId;
}
