package com.colour.follow.service;

import com.colour.follow.domain.dto.FollowDto;
import com.colour.follow.domain.entity.Follow;
import com.colour.follow.repository.FollowRepository;
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
        Follow follow = new Follow(followDto.getFollowerId(), followDto.getFolloweeId());
        repository.follow(follow);
    }

    @Override
    public void unfollowUser(FollowDto followDto) {
        repository.unfollow(followDto.getFollowerId(), followDto.getFolloweeId());
    }
}
