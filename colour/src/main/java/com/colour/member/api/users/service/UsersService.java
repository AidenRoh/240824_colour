package com.colour.member.api.users.service;

import com.colour.board.api.hashtag.domain.dto.HashtagDto;
import com.colour.board.api.post.domain.dto.PostResponseDto;
import com.colour.member.api.follow.domain.dto.FollowDto;
import com.colour.member.api.users.dto.Responsible;
import com.colour.member.api.users.dto.UsersResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UsersService {

    UsersResponseDto getSummary(Long memberId, Pageable pageable);

    Page<PostResponseDto> getPosts(Long memberId, Pageable pageable);

    Page<HashtagDto> getHashtags(Long memberId, Pageable pageable);

    Page<FollowDto> getFollowers(Long memberId, Pageable pageable);

    Page<FollowDto> getFollowees(Long memberId, Pageable pageable);

    Page<Responsible> getDefault(Long memberId, Pageable pageable);


}
