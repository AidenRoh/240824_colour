package com.colour.board.api.post.service;

import com.colour.board.api.post.domain.dto.PostDto;
import com.colour.board.api.post.domain.entity.Post;

import java.util.List;

public interface PostService {

    Post createTemporaryPost(Post post);

    void updatePost(Long postId, PostDto dto, long memberId);

    void deletePost(Long postId, long memberId);

    Post findPostById(Long postId);

    List<Post> findPostByTitle(String title);
}
