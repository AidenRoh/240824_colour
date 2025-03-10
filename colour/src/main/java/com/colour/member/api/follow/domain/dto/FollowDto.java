package com.colour.member.api.follow.domain.dto;

import com.colour.member.api.users.dto.Responsible;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FollowDto implements Responsible {

    private Long followerId;
    private Long followeeId;
}
