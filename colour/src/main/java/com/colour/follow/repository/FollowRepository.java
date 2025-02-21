package com.colour.follow.repository;

import com.colour.follow.domain.entity.Follow;

public interface FollowRepository {

    void follow(Follow follow);

    void unfollow(long followerId, long followeeId);
}
