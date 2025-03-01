package com.colour.board.api.post.service;

import com.colour.board.api.post.domain.dto.PostDto;
import com.colour.board.api.post.domain.entity.PostContent;

public interface PostContentService {

    PostContent createPostContent(PostContent postContent);

    void updatePostContent(Long post_id, PostDto postDto);

    void deletePostContent(Long post_id);
}
