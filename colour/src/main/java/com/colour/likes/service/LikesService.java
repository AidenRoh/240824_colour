package com.colour.likes.service;

public interface LikesService {

    void likePost(long postId, long memberId);

    void dislikePost(long postId, long memberId);
}
