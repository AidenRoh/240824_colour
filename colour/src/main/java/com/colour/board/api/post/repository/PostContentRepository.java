package com.colour.board.api.post.repository;

import com.colour.board.api.post.domain.dto.PostDto;
import com.colour.board.api.post.domain.entity.PostContent;

public interface PostContentRepository {

    PostContent save(PostContent postContent);

    void update(Long postId, PostDto postDto);

    void delete(Long postId);
}
