package com.colour.board.tagtopost.service;

import com.colour.board.tagtopost.dto.TagPostDto;
import com.colour.board.tagtopost.entity.TagPost;

import java.util.List;

public interface TagPostService {

    TagPost save(TagPost tagPost);
    List<TagPost> findByCond(TagPostDto dto);
    void delete(Long tagPostId);
    void delete(Long postId, Long memberId);
}
