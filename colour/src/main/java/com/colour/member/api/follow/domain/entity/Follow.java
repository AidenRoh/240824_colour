package com.colour.member.api.follow.domain.entity;

import lombok.Data;

@Data
public class Follow {

    private Long followerId;
    private Long followeeId;

    public Follow(Long followerId, Long followeeId) {
        this.followerId = followerId;
        this.followeeId = followeeId;
    }
}
