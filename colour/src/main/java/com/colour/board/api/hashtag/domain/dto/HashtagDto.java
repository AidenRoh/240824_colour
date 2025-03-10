package com.colour.board.api.hashtag.domain.dto;

import com.colour.member.api.users.dto.Responsible;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HashtagDto implements Responsible {
    private Long hashtagId;
    private String hashtag;
}
