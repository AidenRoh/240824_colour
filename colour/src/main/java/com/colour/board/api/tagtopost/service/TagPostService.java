package com.colour.board.api.tagtopost.service;

import com.colour.board.api.tagtopost.dto.TagPostDto;
import com.colour.board.api.tagtopost.entity.TagPost;

import java.util.List;

public interface TagPostService {

    TagPost save(TagPost tagPost);

    List<TagPost> findByCond(TagPostDto dto);

    void delete(Long tagPostId);

    void delete(Long postId, Long memberId);
}
