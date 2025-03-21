package com.colour.search.domain.dto;

import com.colour.board.api.colorpalette.domain.dto.ColorCond;
import com.colour.board.api.hashtag.domain.dto.HashtagDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SearchDto {
    List<HashtagDto> hashtags;
    List<String> hexColors;
    List<ColorCond> colorConds;
}
