package com.colour.member.api.users.dto;

import com.colour.board.api.colorpalette.domain.dto.ColorDto;
import com.colour.board.api.hashtag.domain.dto.HashtagDto;
import com.colour.board.api.post.domain.dto.PostResponseDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UsersResponseDto implements Responsible {
    String username;
    Long followers;
    Long followees;
    Long totalLikes;
    List<PostResponseDto> posts;
    List<HashtagDto> hashtags;
    List<ColorDto> colors;

    public UsersResponseDto() {
    }
}
