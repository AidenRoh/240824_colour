package com.colour.board.api.post.repository;

import com.colour.board.api.post.domain.dto.PostRequestDto;
import com.colour.board.api.post.domain.entity.PostContent;

public interface PostContentRepository {

    PostContent save(PostContent postContent);

    void update(Long postId, PostRequestDto postDto);

    void delete(Long postId);
}
