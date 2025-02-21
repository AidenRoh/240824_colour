package com.colour.board.api.likes.domain.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LikesDto {

    private long postId;
    private long memberId;
}
