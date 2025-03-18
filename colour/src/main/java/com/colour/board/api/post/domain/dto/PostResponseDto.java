package com.colour.board.api.post.domain.dto;

import com.colour.board.api.colorpalette.domain.dto.ColorDto;
import com.colour.board.api.hashtag.domain.dto.HashtagDto;
import com.colour.member.api.users.dto.Responsible;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
public class PostResponseDto implements Responsible {
    Long postId;
    String title;
    Long views;
    Long likes;
    Timestamp createdAt;
    Timestamp updatedAt;
    List<HashtagDto> hashtags;
    List<ColorDto> colors;
}
