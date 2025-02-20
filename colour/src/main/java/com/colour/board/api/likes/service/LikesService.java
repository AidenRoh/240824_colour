package com.colour.board.api.likes.service;

public interface LikesService {

    void likePost(long postId, long memberId);

    void dislikePost(long postId, long memberId);
}
