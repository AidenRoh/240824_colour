package com.colour.board.hashtag.service;

import com.colour.board.hashtag.dto.HashtagVo;
import com.colour.board.hashtag.entity.Hashtag;

import java.util.List;

public interface HashtagService {

    Hashtag save(Hashtag hashtag);
    Hashtag findById(Long tagId);
    Hashtag findByTag(String tagName);
    List<Hashtag> findByCond(HashtagVo cond);
    void delete(Long tagId);
}
