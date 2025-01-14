package com.colour.board.post.dto;

import com.colour.board.hashtag.dto.HashtagVo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PostDto {

    private String title;
    private String content;
    private List<ColorVo> colors;
    private List<HashtagVo> hashtags;
}
