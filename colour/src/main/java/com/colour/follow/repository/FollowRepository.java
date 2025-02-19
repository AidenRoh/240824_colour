package com.colour.follow.repository;

public interface FollowRepository {

    void follow(long followerId, long followeeId);

    void unfollow(long followerId, long followeeId);
}
