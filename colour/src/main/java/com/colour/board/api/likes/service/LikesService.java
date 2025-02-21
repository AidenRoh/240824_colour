package com.colour.board.api.likes.service;

import com.colour.board.api.likes.domain.dto.LikesDto;
import com.colour.board.api.likes.domain.entity.Likes;

public interface LikesService {

    void likePost(Likes likes);

    void dislikePost(LikesDto likesDto);

    boolean isLikeExist(LikesDto likesDto);
}
