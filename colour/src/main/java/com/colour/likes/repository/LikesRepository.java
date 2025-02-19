package com.colour.likes.repository;

public interface LikesRepository {

    void saveLike(long postId, long memberId);

    void deleteLike(long postId, long memberId);
}
