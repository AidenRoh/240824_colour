package com.colour.board.api.post.service;

import com.colour.board.api.post.dto.PostDto;
import com.colour.board.api.post.entity.Post;

import java.util.List;

public interface PostService {

    Post createPost(Post post);

    void updatePost(Long postId, PostDto dto);

    Post findPostById(Long postId);

    List<Post> findPostByTitle(String title);

    void deletePost(Long postId);
}
