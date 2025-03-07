package com.colour.member.api.users.repository;

import com.colour.board.api.colorpalette.domain.dto.ColorDto;
import com.colour.board.api.hashtag.domain.dto.HashtagDto;
import com.colour.board.api.post.domain.dto.PostResponseDto;
import com.colour.member.api.follow.domain.dto.FollowDto;
import com.colour.member.api.users.dto.UsersResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UsersRepository {
    UsersResponseDto getCountableInfo(Long memberId);

    List<PostResponseDto> getSummaryPosts(Long memberId);

    List<HashtagDto> getSummaryHashtags(Long memberId);

    List<ColorDto> getSummaryColors(Long memberId);

    Page<PostResponseDto> getPosts(Long memberId, Pageable pageable);

    Page<HashtagDto> getHashtags(Long memberId, Pageable pageable);

    Page<FollowDto> getFollowers(Long memberId, Pageable pageable);

    Page<FollowDto> getFollowees(Long memberId, Pageable pageable);
}
