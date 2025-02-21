package com.colour.follow.domain.entity;

import lombok.Data;

@Data
public class Follow {

    private Long followerId;
    private Long followeeId;

    public Follow(Long followerId, Long followeeId) {
        if (followerId == followeeId) {
            throw new IllegalArgumentException("You cannot follow yourself");
        }
        this.followerId = followerId;
        this.followeeId = followeeId;
    }
}
