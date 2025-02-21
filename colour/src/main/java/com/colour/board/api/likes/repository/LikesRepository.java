package com.colour.board.api.likes.repository;

import com.colour.board.api.likes.domain.entity.Likes;

public interface LikesRepository {

    void save(Likes likes);

    void delete(long postId, long memberId);

    boolean existsByKeys(long postId, long memberId);
}
