package com.colour.member.api.mypage.repository;

import com.colour.board.api.colorpalette.domain.dto.ColorDto;
import com.colour.board.api.hashtag.domain.dto.HashtagDto;
import com.colour.board.api.post.domain.dto.PostResponseDto;
import com.colour.member.api.mypage.dto.MyPageResponseDto;

import java.util.List;

public interface MyPageRepository {
    MyPageResponseDto getCountableInfo(Long memberId);

    List<PostResponseDto> getPosts(Long memberId);

    List<HashtagDto> getHashtags(Long memberId);

    List<ColorDto> getColors(Long memberId);
}
