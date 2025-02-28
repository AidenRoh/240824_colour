package com.colour.board.api.postcolor.repository;

import com.colour.board.api.postcolor.domain.dto.PostColorDto;
import com.colour.board.api.postcolor.domain.entity.PostColor;

import java.util.List;

public interface PostColorRepository {

    void save(PostColor postColor);

    List<PostColor> findByCond(PostColorDto postColorDto);

    void delete(Long postId, Long colorId);

    boolean existsByKeys(Long postId, Long colorId);
}
