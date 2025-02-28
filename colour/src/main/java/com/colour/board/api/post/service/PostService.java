package com.colour.board.api.post.service;

import com.colour.board.api.post.domain.dto.PostDto;
import com.colour.board.api.post.domain.entity.Post;

import java.util.List;

public interface PostService {

    Post createTemporaryPost(Post post);

    void updatePost(Long postId, PostDto dto);

    void deletePost(Long postId);

    Post findPostById(Long postId);

    List<Post> findPostByTitle(String title);
}
