package com.colour.board.api.post.domain.dto;

import com.colour.member.api.users.dto.Responsible;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class PostResponseDto implements Responsible {
    Long postId;
    String title;
    Long views;
    Long likes;
    Timestamp createdAt;
    Timestamp updatedAt;
}
