package com.colour.board.api.posthashtag.service;

import com.colour.board.api.posthashtag.dto.PostHashtagDto;
import com.colour.board.api.posthashtag.entity.PostHashtag;

import java.util.List;

public interface PostHashtagService {

    PostHashtag save(PostHashtag postHashtag);

    List<PostHashtag> findByCond(PostHashtagDto dto);

    void delete(Long postHashtagId);

    void delete(Long postId, Long memberId);
}
