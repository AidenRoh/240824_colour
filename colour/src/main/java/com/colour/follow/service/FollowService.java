package com.colour.follow.service;

import com.colour.follow.domain.dto.FollowDto;

public interface FollowService {
    void followUser(FollowDto followDto);

    void unfollowUser(FollowDto followDto);
}
