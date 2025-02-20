package com.colour.board.api.likes.repository;

public interface LikesRepository {

    void saveLike(long postId, long memberId);

    void deleteLike(long postId, long memberId);
}
