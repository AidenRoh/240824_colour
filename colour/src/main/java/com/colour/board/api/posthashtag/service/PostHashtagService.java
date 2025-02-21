package com.colour.board.api.posthashtag.service;

import com.colour.board.api.posthashtag.domain.dto.PostHashtagDto;
import com.colour.board.api.posthashtag.domain.entity.PostHashtag;

import java.util.List;

public interface PostHashtagService {

    void create(PostHashtag postHashtag);

    List<PostHashtag> findByCond(PostHashtagDto dto);

    void delete(long postId, long hashtagId);

    boolean isPostHashtagExist(long postId, long hashtagId);

}
