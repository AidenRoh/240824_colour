package com.colour.board.api.postcolor.service;

import com.colour.board.api.postcolor.domain.dto.PostColorDto;
import com.colour.board.api.postcolor.domain.entity.PostColor;

import java.util.List;

public interface PostColorService {

    void create(PostColor postColor);

    List<PostColor> findByCond(PostColorDto dto);

    void delete(long postId, long colorId);

    boolean isPostColorExist(long postId, long colorId);
}
