package com.colour.follow.domain.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FollowDto {

    private Long followerId;
    private Long followeeId;
}
