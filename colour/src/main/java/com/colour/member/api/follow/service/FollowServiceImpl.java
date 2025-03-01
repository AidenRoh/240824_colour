package com.colour.member.api.follow.service;

import com.colour.member.api.follow.domain.dto.FollowDto;
import com.colour.member.api.follow.domain.entity.Follow;
import com.colour.member.api.follow.repository.FollowRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FollowServiceImpl implements FollowService {

    private final FollowRepository repository;

    public FollowServiceImpl(FollowRepository repository) {
        this.repository = repository;
    }

    @Override
    public void followUser(FollowDto followDto) {
        Long followerId = followDto.getFollowerId();
        Long followeeId = followDto.getFolloweeId();

        if (followerId.equals(followeeId)) {
            throw new IllegalArgumentException("You cannot follow yourself");
        }

        Follow follow = new Follow(followerId, followeeId);
        repository.follow(follow);
    }

    @Override
    public void unfollowUser(FollowDto followDto) {
        repository.unfollow(followDto.getFollowerId(), followDto.getFolloweeId());
    }
}
