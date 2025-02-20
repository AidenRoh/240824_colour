package com.colour.board.api.tagtopost.repository;

import com.colour.board.api.tagtopost.dto.TagPostDto;
import com.colour.board.api.tagtopost.entity.TagPost;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagPostRepository {

    TagPost save(TagPost tagPost);

    List<TagPost> findByCond(TagPostDto tagPostDto);

    void delete(Long tagPostId);

    void delete(Long postId, Long memberId);
}
