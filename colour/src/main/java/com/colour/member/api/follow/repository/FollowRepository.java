package com.colour.member.api.follow.repository;

import com.colour.member.api.follow.domain.entity.Follow;

public interface FollowRepository {

    void follow(Follow follow);

    void unfollow(long followerId, long followeeId);
}
