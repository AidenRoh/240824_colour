package com.colour.board.api.hashtag.service;

import com.colour.board.api.hashtag.domain.dto.HashtagDto;
import com.colour.board.api.hashtag.domain.entity.Hashtag;

import java.util.List;

public interface HashtagService {

    Hashtag createHashtag(Hashtag hashtag);

    Hashtag findById(Long hashtagId);

    Hashtag findByTag(String hashtagName);

    List<Hashtag> findByCond(HashtagDto cond);

    void deleteHashtag(Long hashtagId);
}
