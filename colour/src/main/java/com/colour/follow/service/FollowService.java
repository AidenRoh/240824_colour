package com.colour.follow.service;

public interface FollowService {
    void followUser(long followerId, long followeeId);

    void unfollowUser(long followerId, long followeeId);
}
