package com.colour.member.api.users.service;

import com.colour.board.api.hashtag.domain.dto.HashtagDto;
import com.colour.board.api.post.domain.dto.PostResponseDto;
import com.colour.member.api.follow.domain.dto.FollowDto;
import com.colour.member.api.users.dto.Responsible;
import com.colour.member.api.users.dto.UsersResponseDto;
import com.colour.member.api.users.repository.UsersRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UsersServiceImpl implements UsersService {

    private final UsersRepository repository;

    public UsersServiceImpl(UsersRepository repository) {
        this.repository = repository;
    }

    @Override
    public UsersResponseDto getSummary(Long memberId, Pageable pageable) {
        UsersResponseDto responseDto = repository.getCountableInfo(memberId);
        responseDto.setPosts(repository.getSummaryPosts(memberId));
        responseDto.setHashtags(repository.getSummaryHashtags(memberId));
        responseDto.setColors(repository.getSummaryColors(memberId));
        return responseDto;
    }

    @Override
    public Page<PostResponseDto> getPosts(Long memberId, Pageable pageable) {
        return repository.getPosts(memberId, pageable);
    }

    @Override
    public Page<HashtagDto> getHashtags(Long memberId, Pageable pageable) {
        return repository.getHashtags(memberId, pageable);
    }

    @Override
    public Page<FollowDto> getFollowers(Long memberId, Pageable pageable) {
        return repository.getFollowers(memberId, pageable);
    }

    @Override
    public Page<FollowDto> getFollowees(Long memberId, Pageable pageable) {
        return repository.getFollowees(memberId, pageable);
    }

    @Override
    public Page<Responsible> getDefault(Long memberId, Pageable pageable) {
        return Page.empty();
    }
}
