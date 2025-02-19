package com.colour.follow.service;

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
    public void followUser(long followerId, long followeeId) {
        repository.follow(followerId, followeeId);
    }

    @Override
    public void unfollowUser(long followerId, long followeeId) {
        repository.unfollow(followerId, followeeId);
    }
}
