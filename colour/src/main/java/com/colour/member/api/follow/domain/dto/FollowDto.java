package com.colour.member.api.follow.domain.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FollowDto {

    private Long followerId;
    private Long followeeId;
}
