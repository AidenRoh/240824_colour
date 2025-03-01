package com.colour.member.api.follow.service;

import com.colour.member.api.follow.domain.dto.FollowDto;

public interface FollowService {
    void followUser(FollowDto followDto);

    void unfollowUser(FollowDto followDto);
}
