package com.colour.member.api.follow.domain.dto;

import com.colour.member.api.users.dto.Responsible;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FollowDto implements Responsible {
    private Long followerId;
    private Long followeeId;
}
